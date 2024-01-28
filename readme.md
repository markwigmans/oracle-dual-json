# Oracle Dual JSON
Explore relational databases through the unique lens of JSON/NoSQL with this demonstrator, 
integrating views from [ElasticSearch](https://www.elastic.co/), [MongoDB](https://www.mongodb.com/), 
and [Oracle 23c](https://www.oracle.com/database/free/) using [Spring Boot](https://spring.io/projects/spring-boot/). 
We examine the performance implications of storing an entire data model as a JSON structure in a single table or document.

## System Overview
The system is designed to:

- Demonstrate the integration of different database technologies.
- Evaluate the performance of storing data in JSON format.
- Provide insights into relational vs. NoSQL database approaches.

Different databases serve as input/output and are synchronized, resulting in the following architecture:

![System Overview](system-overview.png)

The components are:

| Component       | Description                                                         |
|-----------------|---------------------------------------------------------------------|
| Data Service    | Manages the 'Data' data model and API for database synchronization. | 
| ES Service      | Handles ElasticSearch document processing.                          | 
| MongoDB Service | Processes MongoDB documents.                                        |                                                 
| JPA Service     | Manages Oracle document processing.                                 | 

Services are interconnected via a JMS queue, using 'RaceData.topic' for data transmission and 'RaceData.processed' 
for status updates.

## Running the Application

Use the Docker Compose script to start the application:

``docker compose up -d``

### Links

| Item                                                     | Link                                                 | Description                                                      |
|----------------------------------------------------------|------------------------------------------------------|------------------------------------------------------------------|
| API : add data                                           | http://localhost:8080/data/{size}                    | Create {size} of races. All other data is related to that        |
| API : sync data                                          | http://localhost:8180/data/sync                      | Bring all data of databases in sync                              |
| API : example get filter                                 | http://localhost:8180/request/driver/points/{points} | Get all drivers with >= points. Result is Map<Provider, List<?>> |
| JMS management                                           | http://localhost:8161                                | username/password: CNL/CNL                                       |
| [MongoDB viewer](https://hub.docker.com/_/mongo-express) | http://localhost:8081                                | username/password: admin/pass                                    | 
| ElasticSearch Viewer                                     | https://elasticvue.com/                              | install it as browser plugin                                     |
| MetricsVisualisation [Grafana](https://grafana.com/)     | http://localhost:3000/                               | username/password: admin/admin                                   |
| Metrics server [Prometheus](https://prometheus.io/)      | http://localhost:9090/                               |                                                                  |                                

## Configuration
Modify application parameters via [Spring](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config).

| Configuration                  | Description                                                                 |      Default |
|--------------------------------|-----------------------------------------------------------------------------|-------------:|
| odj.data.teamsMultiplier       | multiplier for race size.                                                   |         0.25 |
| odj.data.maxDrivers            | Range of drivers per team.                                                  |           10 |
| odj.data.raceMinLaps           | Minimum laps per race.                                                      |           40 |
| odj.data.raceMaxLaps           | Maximum  laps per race.                                                     |           80 |
| odj.data.racePreviousDays      | Range for race day relative to current day.                                 |          365 |
| odj.data.batch.size            | Batch size for data creation.                                               |         1000 |
| odj.data.batch.message         | Reporting interval for batch processing.                                    |         1000 |
| odj.data.processed.concurrency | Thread count for queue processing.                                          |          1-5 |
| odj.data.threads               | Thread count for topic messages.                                            |     0 (auto) |
| odj.db.providers               | Database providers: 'es' : ElasticSearch, 'mongo' : MongoDB, 'jpa' : Oracle | es,mongo,jpa |

The number of drivers, laps per race, and race day are randomly generated within specified ranges.

## Remarks

### Performance
- Parallel data processing shows potential performance improvements, particularly in high-load scenarios. Further investigation is ongoing.

- UUIDv7 is used in the database as unique key. It is specifically designed to be used in a database context and should
perform better than UUIDv4 (Hibernate default).

### JMS Listeners
Spring Boot uses **@JmsListener** to read from topics and queues. However, the concurrency setting appears to have different behavior. 
For queues if behaves as expected, more readers from the same queue. 
For a given topic it seems  the number of concurrency queues are created, so the same message was processed in parallel without a clear performance win. 
More investigation is need to know what exactly is going on.  

### JSON Relation Duality
Attempts to use Oracle 23c's 'JSON Relational Duality' faced 4Kb record limitations. This led to a shift towards a JSON table/document model.

## Useful Oracle 23c JSON Queries

### Show Data in JSON format
To show all driver data in JSON format with an extra 'type' field:
````sql
SELECT JSON_OBJECT('type' : 'driver', d.*)
FROM driver d
````
output:
```text
{"type":"driver","ID":"018D46231FBE747BB0790E24ABA422C4","COUNTRY":"Polen","NAME":"Mechelina Hogenes","POINTS":51,"TEAM_ID":"018D46231FBE747BB0789874A44FA1AD"}
{"type":"driver","ID":"018D46231FCB73448C5BCE9DD9C515B2","COUNTRY":"Argentinië","NAME":"Dawn Sturme IV","POINTS":38,"TEAM_ID":"018D46231FCB73448C5A8CA242CF9F8A"}
{"type":"driver","ID":"018D46231FCC78A896B35E0C356C352B","COUNTRY":"Benin","NAME":"Morris Hesp","POINTS":103,"TEAM_ID":"018D46231FCB73448C5A8CA242CF9F8A"}
{"type":"driver","ID":"018D46231FCC78A896B41334341C39DA","COUNTRY":"Niger","NAME":"Luitzen de Dirksen","POINTS":55,"TEAM_ID":"018D46231FCB73448C5A8CA242CF9F8A"}
...
```

### Show JSON Data
A normal json query is limited by 4K output. The following query works with larger outputs as well:

````sql
SELECT json_query(json, '$' returning clob pretty) AS json_data 
FROM OUTPUT_DOCUMENT
````
output:

````text
{
  "id" : "018d4623-6649-7ed8-b835-8c6994afe492",
  "refId" : "018d4623-2036-7cdd-aad0-0c737a02350b",
  "driver" :
  {
    "refId" : "018d4623-2036-7cdd-aad0-0c737a02350b",
    "name" : "Bsc Suzet Snaijer",
    "country" : "Salomon",
    "points" : 144,
....
````
**Note:** 'returning clob' is essential for displaying larger content.

## TODO List
- [x] Dynamic Query building based on request;
- [ ] Explicit performance measurements to support claims about performance improvements.
- [x] Select which DB to use (1,2 or all of them)

## Further Information
- [JSON Relational Duality](https://github.com/oracle-samples/oracle-db-examples/blob/main/json-relational-duality/DualityViewTutorial.sql)
- [MongoDB interface on Oracle](https://blogs.oracle.com/database/post/installing-database-api-for-mongodb-for-any-oracle-database)
