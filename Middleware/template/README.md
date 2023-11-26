### Pre-requisites for building the service
Java 17 or above installed with environment variable set.

[Maven](https://maven.apache.org/download.cgi) installed with environment variable set.

### How to build
1. Open a terminal.
2. Go to the root folder of the service. (`...\toothtrek\Middleware\template`)
2. Run the command `mvn clean install -DskipTests` (optionally)

### How to run the service
1. Set the following Environment Variables:

    * `DB_HOST`: The host of the database. (e.g. `localhost`)
    * `DB_PORT`: The port of the database. (e.g. `5432`)
    * `DB_NAME`: The name of the database. (e.g. `postgres`)
    * `DB_USERNAME`: The username of the database. (e.g. `postgres`)
    * `DB_PASSWORD`: The password of the database. (e.g. `password`)

2. Go to the target folder  `cd target`
3. Run the command `java -jar <jar-file-name>.jar <broker-address> <client-id> <quality-of-service>`

    * `<jar-file-name>`: The name of the jar file. (e.g. `template-0.0.1-SNAPSHOT.jar`)
    * `<broker-address>`: The address of the broker. (e.g. `tcp://localhost:1883`)
    * `<client-id>`: The client id of the service or `random` for UUID. (e.g. `client-1` or `random`)
    * `<quality-of-service>`: The quality of service of the service. (0, 1, 2)

##### Full Command Example: `java -jar template-0.0.1-SNAPSHOT.jar tcp://localhost:1883 client-1 0`

### How to run tests locally through Maven
1. Open a terminal.
2. Set the following Environment Variables:

    * `DB_HOST`: The host of the database. (e.g. `localhost`)
    * `DB_PORT`: The port of the database. (e.g. `5432`)
    * `DB_NAME`: The name of the database. (e.g. `template`)
    * `DB_USERNAME`: The username of the account. (e.g. `postgres`)
    * `DB_PASSWORD`: The password of the account. (e.g. `password`)

3. Go to the root folder of the service. (`...\toothtrek\Middleware\template`)
4. Run the command `mvn test`