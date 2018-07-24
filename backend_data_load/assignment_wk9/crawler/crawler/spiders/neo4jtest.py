# from neo4j.v1 import GraphDatabase, basic_auth
#
# driver = GraphDatabase.driver("bolt://localhost:7687", auth=basic_auth("neo4j", "123456"))
# session = driver.session()
#
# result = session.run("MATCH (a:AUTHOR) WHERE a.name = {name} RETURN a.name as name",
#                        {"name": "Jia Zhang"})
# for record in result:
#     print record['name']
#
# session.close()
