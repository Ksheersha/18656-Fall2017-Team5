import scrapy
import urllib2
import xml.etree.ElementTree as ET

from scrapy import Request

from scrapy.loader import ItemLoader
from crawler.items import Paper

class PaperSpider(scrapy.Spider):
    name = 'paper_crawler'
    custom_settings = {
        'ITEM_PIPELINES': {
            'crawler.pipelines.SavePaperToNeo4j': 100,
        }
    }

    # Get the ICSW url from web API
    def getICSWUrl(self):
        data = urllib2.urlopen("http://dblp.org/search/venue/api?q=^icws$").read()
        # print data
        root = ET.fromstring(data)
        for child in root:
            if child.tag == 'hits':
                url = child[0].find('info').find('url').text
                return url

    # init
    def __init__(self, category=None, *args, **kwargs):
        super(PaperSpider, self).__init__(*args, **kwargs)
        url = self.getICSWUrl()
        self.start_urls = [url]

    # parsing icsw page
    def parse(self, response):
        conference_selector = response.css("ul.publ-list")
        icsw2017_data_selector = conference_selector[0].css("div.data")

        icsw_url = icsw2017_data_selector.css("div.data > a::attr(href)").extract_first()
        conftitle = icsw2017_data_selector.css("span.title::text").extract_first()
        confPublisher = icsw2017_data_selector.css("span[itemprop='publisher']::text").extract_first()
        confISBN = icsw2017_data_selector.css("span[itemprop='isbn']::text").extract_first()

        yield Request(url=icsw_url, callback=self.parse_papers)

    # parsing all papers in icsw 2017 page
    def parse_papers(self, response):
        researchTracks = response.css("ul.publ-list")

        if researchTracks:
            for researchTrack in researchTracks:
                publications = researchTrack.css("li.entry")
                if publications:
                    for publication in publications:

                        loader = ItemLoader(item=Paper(), response=response)
                        #For each paper, extract its metadata information (e.g., paper title, page numbers, and author names);
                        authors = publication.css("div.data > span[itemprop='author'] > a > span[itemprop='name']::text").extract()
                        publicationId = publication.css("li.entry::attr('id')").extract_first()
                        publicationType = publication.css("li.entry::attr('itemtype')").extract_first()
                        publicationTitle = publication.css("div.data > span.title::text").extract_first()
                        publicationPage = publication.css("div.data > span[itemprop='pagination']::text").extract_first()

                        loader.add_value('authors', authors)
                        loader.add_value('pubId', publicationId)
                        loader.add_value('pubType', publicationType)
                        loader.add_value('pubTitle', publicationTitle)
                        loader.add_value('pubPage', publicationPage)

                        yield loader.load_item()
