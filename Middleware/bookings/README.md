### Pre-requisites for building the service
Java 17 or above installed with environment variable set.

[Maven](https://maven.apache.org/download.cgi) installed with environment variable set.

### How to build & run a specific service
1. Open a terminal.
2. Go to the root folder of the service. (`...\toothtrek\Middleware\template`)
2. Run the command `mvn clean install`
3. Go to the target folder  `cd target`
4. Run the command `java -jar <jar-file-name>.jar <broker-address> <client-id> <quality-of-service>`

    * `<jar-file-name>`: The name of the jar file. (e.g. `bookings-0.0.1-SNAPSHOT.jar`)
    * `<broker-address>`: The address of the broker. (e.g. `tcp://localhost:1883`)
    * `<client-id>`: The client id of the service or `random` for UUID. (e.g. `client-1` or `random`)
    * `<quality-of-service>`: The quality of service of the service. (0, 1, 2)

##### Full Command Example: `java -jar bookings-0.0.1-SNAPSHOT.jar tcp://localhost:1883 client-1 0`

### How to run tests through Maven
1. Open a terminal.
2. Go to the root folder of the service. (`...\toothtrek\Middleware\bookings`)
3. Run the command `mvn test`