# Create Oracle Image (APEX/ORDS)
## Create Image
The following steps are performed to create the start container image.

Copy the ORDS connection string to connect the ORDS to the database.

````shell
cp db/ords/conn_string.txt ../data/ords/variables/
````

Start docker compose.
````shell
docker compose up -d
````

#### Enable MongoDB
Wait till logging appears in the ORDS container.

Goto ORDS container and enable mongodb.
````shell
docker compose exec -it ords /bin/bash
````
````shell
ords config set mongo.enabled true
ords serve
````
It shows the URL to be used
``
mongodb://[{user}:{password}@]localhost:27017/{user}?authMechanism=PLAIN&authSource=$external&ssl=true&retryWrites=false&loadBalanced=true
``


## Safe Created Image
Get ID with, for example: 419e12f1fe95
````shell
docker ps
````

If needed (only once) login, into the github account.
````shell
docker login --username markwigmans --password <PAT Token> ghcr.io
````

````shell
docker commit 419e12f1fe95 ghcr.io/markwigmans/23cfree
docker push ghcr.io/markwigmans/23cfree
````


http://localhost:8023/ords/sql-developer

https://github.com/oracle-samples/oracle-db-examples/blob/main/json-relational-duality/DualityViewTutorial.sql

https://blogs.oracle.com/database/post/installing-database-api-for-mongodb-for-any-oracle-database


-----

https://container-registry.oracle.com/ords/f?p=113:4:114061512426278:::4:P4_REPOSITORY,AI_REPOSITORY,AI_REPOSITORY_NAME,P4_REPOSITORY_NAME,P4_EULA_ID,P4_BUSINESS_AREA_ID:1183,1183,Oracle%20REST%20Data%20Services%20(ORDS)%20with%20Application%20Express,Oracle%20REST%20Data%20Services%20(ORDS)%20with%20Application%20Express,1,0&cs=3EpJNpVY6GKrYjOQhIaUm-xGEbF2ECXCzGTEJo-wPV0e2kK1ITM-PaxOP5E9VTBCw7GL7kI68z91tkoKwDDuWPQ
