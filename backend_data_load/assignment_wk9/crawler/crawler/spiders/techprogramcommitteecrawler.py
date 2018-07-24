import scrapy
from scrapy import Request
import csv

from neo4j.v1 import GraphDatabase, basic_auth
import urllib, urllib2, httplib,  json
from urlparse import urlparse

from scrapy.loader import ItemLoader
from crawler.items import AuthorMetaData

# Replace the subscriptionKey string value with your valid subscription key.
subscriptionKey = ""
host = "api.cognitive.microsoft.com"
path = "/bing/v7.0/search"

class ProgramCommitteeCrawler(scrapy.Spider):
    driver = GraphDatabase.driver("bolt://localhost:7687", auth=basic_auth("neo4j", "123456"))
    name = 'technical_committee_crawler'
    start_urls = (
        'http://icws.org/2017/organization.html',
    )
    custom_settings = {
        'ITEM_PIPELINES': {
            'crawler.pipelines.WriteToFile': 100,
        }
    }
    # parse google scholar page to take researcher information
    def parseGoogleScholar(self, response):
        imagePath = response.css("div#gsc_prf_pua > img::attr(src)").extract_first()
        imageUrl = '%s%s' % ('https://scholar.google.com', imagePath)

        researchInterests = response.css("div#gsc_prf_int > a::text").extract()
        researchStr = ', '.join(researchInterests)

        try:
            institution_sel = response.css('div.gsc_prf_il')[0]
            institution = response.css('div.gsc_prf_il::text').extract_first()
            if not institution:
                institution = response.css('div.gsc_prf_il > a.gsc_prf_ila::text').extract_first()
        except:
            institution = 'N/A'

        loader = ItemLoader(item=AuthorMetaData(), response=response)
        loader.add_value('index', response.meta['index'])
        loader.add_value('name', response.meta['name'])
        loader.add_value('img_url', imageUrl)
        loader.add_value('research_interests', researchStr)
        loader.add_value('homepage_url', response.url)
        loader.add_value('institution', institution)

        print '%s\t%s\t%s\t%s\t%s ' % (response.meta['index'], response.meta['name'], imageUrl, institution, ', '.join(researchInterests))
        yield loader.load_item()


    # Call bing search API to get url of google scolar citation
    def findResearcherHomePage(self, name, institution):
        queryText = '%s %s Google Scholar' % (name, institution)
        headers = {'Ocp-Apim-Subscription-Key': subscriptionKey}
        conn = httplib.HTTPSConnection(host)
        values = {'q': queryText}
        query = urllib.urlencode(values)
        conn.request("GET", path + "?" + query, headers=headers)
        response = conn.getresponse()
        headers = [k + ": " + v for (k, v) in response.getheaders()
                       if k.startswith("BingAPIs-") or k.startswith("X-MSEdge-")]
        return response.read().decode("utf8")

    # parse conference organization page
    def parse(self, response):

        with open('../../../import/author.csv', 'rb') as csvfile:
            authors_reader = csv.reader(csvfile, delimiter=',')
            next(authors_reader, None)

            authors = []
            for row in authors_reader:
                author = {}
                author['index'] = row[0]
                author['name'] = row[1]
                authors.append(author)

        # print authors

        # for every tc member, find theier personal homepage
        for author in authors:
            result = self.findResearcherHomePage(author['name'],'')
            data = json.loads(result)['webPages']['value']
            googleurl = ''
            for obj in data:
                parsedUrl = urlparse(obj['url'])
                if parsedUrl.netloc == 'scholar.google.com':
                    googleurl = obj['url']
                    break
            if not googleurl == '':
                yield Request(url=googleurl, callback=self.parseGoogleScholar, meta={'index': author['index'], 'name': author['name']})
