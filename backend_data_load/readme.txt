Steps to import data into Neo4j database:
1. Initialize a new Neo4j database service as instructed. A "neo4j" folder should be generated.
2. Open this "neo4j" folder. Copy all .csv files under current "import/" folder into your "neo4j/import/" folder.
3. Open your Neo4j database's UI. Run all the cypher code in "import_cyphor_code.txt" line by line. All command should be working without any problem.

*Note: The backend schema should be quite clear by reading the cypher code itself.

4. Generating person-person data:
* Install virtualenv in your machine
* run `virtualenv -p python env`
* run `source env/bin/activate`
* run `pip install -r requirements.txt`
* run `python generate_person_person.py`
