import csv
from neo4j.v1 import GraphDatabase, basic_auth

driver = GraphDatabase.driver("bolt://localhost:7687", auth=basic_auth("neo4j", "123456"))
session = driver.session()

with open('import/author_metadata.csv', 'rb') as csvfile:
    authors = csv.reader(csvfile, delimiter='\t')
    # print authors

    for row in authors:
        print row
        session.run("MATCH (a:Author {index: {index}}) SET a.picture = {picture} SET a.interests = {interests} SET a.homepage = {homepage} SET a.affiliation = {affiliation} RETURN a",
            {
            "index": row[0],
            "picture": row[2],
            "interests": row[5],
            "homepage": row[3],
            "affiliation": row[4]})

    session.close()

session.close()
