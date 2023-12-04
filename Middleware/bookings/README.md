# Bookings Service

### Pre-requisites for building the service
Java 17 or above installed with environment variable set.

[Maven](https://maven.apache.org/download.cgi) installed with environment variable set.

### How to build
1. Open a terminal.
2. Go to the root folder of the service. (`...\toothtrek\Middleware\bookings`)
2. Run the command `mvn clean install -DskipTests`

### How to run the service
1. Set the following Environment Variables:

    * `DB_HOST`: The host of the database. (e.g. `localhost`)
    * `DB_PORT`: The port of the database. (e.g. `5432`)
    * `DB_NAME`: The name of the database. (e.g. `bookings`)
    * `DB_USERNAME`: The username of the database. (e.g. `postgres`)
    * `DB_PASSWORD`: The password of the database. (e.g. `password`)
    * `MQTT_BROKER`: The address of the broker. (e.g. `tcp://localhost:1883`)
    * `MQTT_QOS`: The quality of service of the service. (0, 1, 2)
    * `MQTT_CLIENT_ID`: The client id of the service or empty for UUID. (e.g. `client-1`)

2. Go to the target folder  `cd target`
3. Run the command `java -jar <jar-file-name>.jar`

    * `<jar-file-name>`: The name of the jar file. (e.g. `bookings-0.0.1-SNAPSHOT.jar`)

##### Full Command Example: `java -jar bookings-0.0.1-SNAPSHOT.jar`
##### Environment Variables Example:

```
    set DB_HOST=localhost
    set DB_PORT=5432
    set DB_NAME=bookings
    set DB_USERNAME=postgres
    set DB_PASSWORD=<password>
    set MQTT_BROKER=tcp://localhost:1883
    set MQTT_QOS=1
    set MQTT_CLIENT_ID=client-1 #Optional
```

### How to run tests through Maven
1. Open a terminal.
2. Go to the root folder of the service. (`...\toothtrek\Middleware\bookings`)
3. Run the command `mvn test`

### For developers

#### How to rebuild and run the service in one command (Windows)
1. Open a terminal.
2. Go to the target folder of the service. (`...\toothtrek\Middleware\bookings\target`)
3. Run the command
4. `cd .. & mvn clean install -DskipTests & cd target & java -jar <jar-file-name>.jar`

    * `<jar-file-name>`: The name of the jar file. (e.g. `bookings-0.0.1-SNAPSHOT.jar`)

##### Full Command Example: `cd .. & mvn clean install -DskipTests & cd target & java -jar bookings-0.0.1-SNAPSHOT.jar`