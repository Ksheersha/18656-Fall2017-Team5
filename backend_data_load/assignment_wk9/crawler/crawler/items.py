# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy

class Paper(scrapy.Item):
    authors = scrapy.Field()
    pubId = scrapy.Field()
    pubType = scrapy.Field()
    pubTitle = scrapy.Field()
    pubPage = scrapy.Field()

class AuthorMetaData(scrapy.Item):
    index = scrapy.Field()
    institution = scrapy.Field()
    name = scrapy.Field()
    img_url = scrapy.Field()
    research_interests = scrapy.Field()
    homepage_url = scrapy.Field()
