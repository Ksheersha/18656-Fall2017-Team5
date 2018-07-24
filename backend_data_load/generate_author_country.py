import csv
from neo4j.v1 import GraphDatabase, basic_auth
from random import randint

driver = GraphDatabase.driver("bolt://localhost:7687", auth=basic_auth("neo4j", "123456"))
session = driver.session()

countries = ['USA','Germany','China','Indonesia','India','Singapore','Thailand','Canada','Malaysia','Japan']

with open('import/author.csv', 'rb') as csvfile:
    authors = csv.reader(csvfile, delimiter=',')
    # print authors

    for row in authors:
        countryIdx = randint(0, len(countries)-1)
        # print row[0] + ' ' + countries[countryIdx]
        session.run("MATCH (a:Author {index: {index}}) SET a.country = {country} RETURN a",
            {
            "index": row[0],
            "country": countries[countryIdx]})

session.close()
