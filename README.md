# 18656-Fall2017-Team5

## Background

The innovations of SOC also offer many interesting avenues of research for scientific and industrial communities. Recent advances in service oriented computing and cloud computing, including computational power, storage, and networking, and infrastructure innovations, are providing exciting opportunities to make significant progress in understanding and addressing complex real-world challenges. In this project, we aimed to design and develop a software system focusing on collecting, aggregating, classifying, analyzing, reporting, and visualizing data related to Service Oriented Software Engineering (SOSE) research community.

## Setup

* git clone  git@git@github.com:cmusv-sc/18656-Fall2017-Team5.git
* Install Neo4J database and use neo4j as username and 123456 as password.
* You need to populate the database with pre-processed data we have. Please go to backend_data_load folder and see how to populate the neo4j database with our pre-processed data.
* Install activator/sbt to run the backend code. To run the backend code, use this following command:
```
activator run 		
```
* Install node and npm to run the frontend code. To run the frontend code, use this following command:
```
npm install
npm start
```
* You can access the project from your browser by using http://localhost:3000 as the URL.
