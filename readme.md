# Oracle Dual JSON
Explore relational databases through a JSON/NoSQL lens with this demonstrator, 
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

Services are connected via a JMS queue, using 'RaceData.topic' for data transmission and 'RaceData.processed' 
for processing status.

## Running the Application

Start the application with the provided Docker Compose script:

``docker compose up -d``

### Links

| Item                                                     | Link                              | Description                                               |
|----------------------------------------------------------|-----------------------------------|-----------------------------------------------------------|
| API : add data                                           | http://localhost:8080/data/{size} | Create {size} of races. All other data is related to that |
| API : sync data                                          | http://localhost:8180/data/sync   | Bring all data of databases in sync                       |
| JMS management                                           | http://localhost:8161             | username/password: CNL/CNL                                |
| [MongoDB viewer](https://hub.docker.com/_/mongo-express) | http://localhost:8081             | username/password: admin/pass                             | 
| ElasticSearch Viewer                                     | https://elasticvue.com/           | install it as browser plugin                              |

## Configuration
The following application parameters can be set. This is all done via [Spring](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config).

| Configuration                  | Description                                                                 |      Default |
|--------------------------------|-----------------------------------------------------------------------------|-------------:|
| odj.data.teamsMultiplier       | multiplier for race size                                                    |         0.25 |
| odj.data.maxDrivers            | teams contains [ maxdrivers/2 - maxdriver ] drivers                         |           10 |
| odj.data.raceMinLaps           | Minimum laps per race.                                                      |           40 |
| odj.data.raceMaxLaps           | Maximum  laps per race.                                                     |           80 |
| odj.data.racePreviousDays      | race day is [ 0 - racePreviousDays ] from current day                       |          365 |
| odj.data.batch.size            | Batch size for data creation.                                               |         1000 |
| odj.data.batch.message         | Report every x size                                                         |         1000 |
| odj.data.processed.concurrency | Thread count for queue processing.                                          |          1-5 |
| odj.data.threads               | Thread count for topic messages (0 = auto).                                 |            0 |
| odj.db.providers               | Database providers: 'es' : ElasticSearch, 'mongo' : MongoDB, 'jpa' : Oracle | es,mongo,jpa |

The number of drivers, the number of laps of a given race and the race day are all generated from a random value in the given range.

## Remarks

### Performance
Parallel data processing shows potential performance improvements, particularly in high-load scenarios. Further investigation is ongoing.

UUIDv7 is used in the database as unique key. It is specifically designed to be used in a database context and should
perform better than UUIDv4 (Hibernate default).

### JMS Listeners
Spring Boot uses **@JmsListener** to read from topics and queues. However, the concurrency setting appears to have different behavior. 
For queues if behaves as expected, more readers from the same queue. 
For a given topic it seems  the number of concurrency queues are created, so the same message was processed in parallel without a clear performance win. 
More investigation is need to know what exactly is going on.  

### JSON Relation Duality
Attempts to use Oracle 23c's 'JSON Relational Duality' faced 4Kb record limitations. This led to a shift towards a JSON table/document model.

## TODO List
- [ ] Dynamic Query building based on request;
- [ ] Explicit performance measurements to support claims about performance improvements.
- [x] Select which DB to use (1,2 or all of them)

## Further Information
- [JSON Relational Duality](https://github.com/oracle-samples/oracle-db-examples/blob/main/json-relational-duality/DualityViewTutorial.sql)
- [MongoDB interface on Oracle](https://blogs.oracle.com/database/post/installing-database-api-for-mongodb-for-any-oracle-database)
