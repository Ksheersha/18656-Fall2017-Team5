import csv
from neo4j.v1 import GraphDatabase, basic_auth

driver = GraphDatabase.driver("bolt://localhost:7687", auth=basic_auth("neo4j", "123456"))
session = driver.session()

with open('import/paper_cite.csv', 'rb') as csvfile:
    paper_cite = csv.reader(csvfile, delimiter=',')
    next(paper_cite, None)

    for row in paper_cite:
        session.run("MATCH (a:Paper { index: {a} }),(b:Paper { index: {b} }) MERGE (a)-[r:CITE]->(b)",
            {"a": row[0], "b":row[1]})

session.close()
