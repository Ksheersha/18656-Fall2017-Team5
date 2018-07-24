# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html

from neo4j.v1 import GraphDatabase, basic_auth
from scrapy.exceptions import CloseSpider, DropItem


class SavePaperToNeo4j(object):
    def process_item(self, item, spider):
        authors = item.get('authors')
        title = item.get('pubTitle', '')
        paperId = item.get('pubId')
        paperType = item.get('pubType')
        paperPage = item.get('pubPage')

        if title == '':
            raise DropItem('Item Not Valid')

        driver = GraphDatabase.driver("bolt://localhost:7687", auth=basic_auth("neo4j", "123456"))
        session = driver.session()

        session.run("MERGE (p:PAPER {id:{paperId}}) ON CREATE SET p.title = {title} SET p.id = {paperId} SET p.type ={paperType} SET p.page={paperPage})",
            {"title": title, "paperId":paperId, "paperType":paperType, "paperPage":paperPage})

        for author in authors:
            # session.run("CREATE (a:AUTHOR {name: {name}})", {"name": author})
            session.run("MERGE (a:AUTHOR { name: {name} }) ON CREATE SET a.name = {name} return a", {"name": author})
            session.run("MATCH (a:AUTHOR {name: {authorName}}), (p:PAPER {id:{paperId}}) "
                "CREATE (a)-[:WRITES]->(p)",
                {"authorName" : author, "paperId": paperId})
        session.close()

        return item

class AddAuthorMetadata(object):
    def process_item(self, item, spider):
        authorName = item.get('name')
        imageUrl = item.get('img_url')
        researchInterests = item.get('research_interests')
        homepageUrl = item.get('homepage_url')

        driver = GraphDatabase.driver("bolt://localhost:7687", auth=basic_auth("neo4j", "123456"))
        session = driver.session()
        session.run("MATCH (a:AUTHOR {name: {name}}) SET a.img_url = {img_url} SET a.research_interests = {research_interests} SET a.homepage_url = {homepage_url} RETURN a",
            {"name" : authorName[0], "img_url": imageUrl[0], "research_interests": researchInterests[0],
            "homepage_url":homepageUrl[0]})

        session.close()

        return item

class WriteToFile(object):

    def __init__(self):
        print 'test'

    def process_item(self, item, spider):
        authorName = item.get('name')
        imageUrl = item.get('img_url')
        researchInterests = item.get('research_interests')
        homepageUrl = item.get('homepage_url')
        institution = item.get('institution')
        index = item.get('index')

        f = open('output', 'a')
        f.write("%s\t%s\t%s\t%s\t%s\t%s\n" % (index[0], authorName[0], imageUrl[0], homepageUrl[0],institution[0], researchInterests[0]))
        f.close()
        return item
