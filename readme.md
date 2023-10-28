# Create Oracle Image (APEX/ORDS)

## Create Image
The following steps are performed in line with [Oracle Golden Image](https://pretius.com/blog/oracle-apex-docker-ords/).

### Steps

````console
docker run -d -it --name 23cfree -p 8521:1521 -p 8500:5500 -p 8023:8080 -p 9043:8443 -e ORACLE_PWD=E container-registry.oracle.com/database/free:latest
````

````console
docker exec -it 23cfree /bin/bash
````

#### Install APEX
````shell
curl -o apex-latest.zip https://download.oracle.com/otn_software/apex/apex-latest.zip

unzip apex-latest.zip
rm apex-latest.zip
cd apex
````

``
sqlplus / as sysdba
``

````shell
ALTER SESSION SET CONTAINER = FREEPDB1;
@apexins.sql SYSAUX SYSAUX TEMP /i/
````

````shell
ALTER USER APEX_PUBLIC_USER ACCOUNT UNLOCK;
ALTER USER APEX_PUBLIC_USER IDENTIFIED BY E;
````

````shell
@apxchpwd.sql
````
Password uses is: Secret!123

````shell
mkdir /home/oracle/software
mkdir /home/oracle/software/apex
mkdir /home/oracle/software/ords
mkdir /home/oracle/scripts
````

````shell
cp -r /home/oracle/apex/images /home/oracle/software/apex
cd /home/oracle/
````

````shell
su
dnf update
dnf install sudo -y
dnf install nano -y
````

Update Sudo list
````shell
nano /etc/sudoers
````

````shell
Defaults !lecture
````

````shell
oracle ALL=(ALL) NOPASSWD: ALL
````

````shell
dnf install java-17-openjdk -y
````

````shell
mkdir /etc/ords
mkdir /etc/ords/config
mkdir /home/oracle/logs
chmod -R 777 /etc/ords
java -version
````
#### Install ORDS
````shell
yum-config-manager --add-repo=http://yum.oracle.com/repo/OracleLinux/OL8/oracle/software/x86_64
dnf install ords -y
````

````shell
export _JAVA_OPTIONS="-Xms512M -Xmx512M"
ords --config /etc/ords/config install
````
The installation should look like:
````console
Installation Type > Choose option [2] Enter
Connection Type > Choose option [1] Enter
host name > Enter
listen port > Enter
service name > FREEPDB1
administrator username > SYS
password > E
default tablespace > Enter
temp tablespace > Enter
features > Enter
Start ORDS > [1] Enter <-- Standalone Mode
protocol > [1] < http
port > [1] <-- 8080
Static Resources > /home/oracle/software/apex/images
````

run in host browser:
``
http://localhost:8023/ords/apex
``
Stop application via `Ctrl-C`

#### Enable MongoDB
````shell
ords config set mongo.enabled true
ords serve
````
It shows the URL to be used
``
mongodb://[{user}:{password}@]localhost:27017/{user}?authMechanism=PLAIN&authSource=$external&ssl=true&retryWrites=false&loadBalanced=true
``

````shell
nano /home/oracle/scripts/start_ords.sh
````

````console
export ORDS_HOME=/usr/local/bin/ords
export _JAVA_OPTIONS="-Xms512M -Xmx512M"
LOGFILE=/home/oracle/logs/ords-`date +"%Y""%m""%d"`.log
nohup ${ORDS_HOME} --config /etc/ords/config serve >> $LOGFILE 2>&1 & echo "View log file with : tail -f $LOGFILE"
````

````shell
nano /home/oracle/scripts/stop_ords.sh
````
````console
kill `ps -ef | grep [o]rds.war | awk '{print $2}'`
````

````shell
nano /opt/oracle/scripts/startup/01_auto_ords.sh
````
````console
sudo sh /home/oracle/scripts/start_ords.sh
````

````shell
docker restart 23cfree
````

In host browser
``
http://localhost:8023/ords/apex.
``

````console
Workspace > INTERNAL
Username > ADMIN
Password > Secret!123
````

#### Create Local User
````console
docker exec -it 23cfree /bin/bash
````
````shell
sqlplus / as sysdba
````

`````console
ALTER SESSION SET CONTAINER = FREEPDB1;
CREATE USER IF NOT EXISTS MARK IDENTIFIED BY "MARK";
GRANT ALL PRIVILEGES TO MARK;
quit
`````

````shell
sqlplus MARK/MARK@freepdb1
````
Enable MongoDB for given schema
`````console
exec ords.enable_schema(true)
quit
`````

## Safe Created Image
Get ID with, for example: 419e12f1fe95
````shell
docker ps
````

````shell
docker commit 419e12f1fe95 ghcr.io/markwigmans/23cfree
docker login --username markwigmans --password <PAT Token> ghcr.io
docker push ghcr.io/markwigmans/23cfree
````


http://localhost:8023/ords/sql-developer

https://github.com/oracle-samples/oracle-db-examples/blob/main/json-relational-duality/DualityViewTutorial.sql

https://blogs.oracle.com/database/post/installing-database-api-for-mongodb-for-any-oracle-database