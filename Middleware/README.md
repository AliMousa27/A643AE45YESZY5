### Pre-requisites for building the services
Maven installed with environment variable set.

[Link to download](https://maven.apache.org/download.cgi)

### How to build & run a specific service
1. Open a terminal.
2. Go to the root folder of the service. (`...\toothtrek\Middleware\template`)
2. Run the command `mvn clean install`
3. Go to the target folder.
4. Run the command `java -jar <jar-file-name>.jar <broker-address> <client-id> <quality-of-service>`

    * `<jar-file-name>`: The name of the jar file. Run the one with the suffix `-jar-with-dependencies`. (e.g. `template-1.0-SNAPSHOT-jar-with-dependencies.jar`)
    * `<broker-address>`: The address of the broker. (e.g. `tcp://localhost:1883`)
    * `<client-id>`: The client id of the service. (e.g. `client-1`)
    * `<quality-of-service>`: The quality of service of the service. (0, 1, 2)

##### Full Command Example: `java -jar template-1.0-SNAPSHOT-jar-with-dependencies.jar tcp://localhost:1883 client-1 0`

### How to run tests through Maven
1. Open a terminal.
2. Go to the root folder of the service. (`...\toothtrek\Middleware\template`)
3. Run the command `mvn test`