import csv
from neo4j.v1 import GraphDatabase, basic_auth

driver = GraphDatabase.driver("bolt://localhost:7687", auth=basic_auth("neo4j", "123456"))
session = driver.session()

with open('import/paper_author.csv', 'rb') as csvfile:
    paper_author = csv.reader(csvfile, delimiter=',')
    next(paper_author, None)

    paper = {}
    for row in paper_author:
        if row[0] in paper:
            authors = paper[row[0]]
            authors.append(row[1])
            paper[row[0]] = authors
        else:
            authors = []
            authors.append(row[1])
            paper[row[0]] = authors

    for key, value in paper.iteritems():
        print value
        for i in range(0, len(value)):
            for j in range(i, len(value)):
                if i != j:
                    print '%s %s' % (value[i], value[j])
                    session.run("MATCH (a:Author { index: {a} }),(b:Author { index: {b} }) MERGE (a)-[r:COAUTHOR]-(b)",
                                {"a": value[i], "b":value[j]})

session.close()
