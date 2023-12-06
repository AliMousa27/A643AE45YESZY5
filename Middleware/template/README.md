# Template Service

### Pre-requisites for building the service
Java 17 or above.

[Maven](https://maven.apache.org/download.cgi) installed with environment variable set.

### How to build
1. Open a terminal.
2. Go to the root folder of the service. (`...\template`)
2. Run the command `mvn clean install -DskipTests`

### How to run the service
1. Go to `.../template/src/main/resources` and create a file named `.env`
   - See the file `.env.example` under the same directory for an example setup and the necessary variables.
   - **Important**: The service will not run without the `.env` file or if it is not configured properly.
   - **Note**: You must rebuild the service after changing or creating the `.env` file to apply the changes.

2. Go to the target folder  `.../template/target`
3. Run the command `java -jar <jar-file-name>.jar`

    * `<jar-file-name>`: The name of the jar file. (e.g. `template-0.0.1-SNAPSHOT.jar`)

##### Full Command Example: `java -jar template-0.0.1-SNAPSHOT.jar`

### How to run tests locally through Maven
1. Open a terminal.
2. Go to the root folder of the service. (`...\template`)
3. Run the command `mvn test`

### For developers

#### How to rebuild and run the service in one command (Windows CMD)
1. Open a terminal.
2. Go to the target folder of the service. (`...\template\target`)
3. Run the command
4. `cd .. & mvn clean install -DskipTests & cd target & java -jar <jar-file-name>.jar`

    * `<jar-file-name>`: The name of the jar file. (e.g. `template-0.0.1-SNAPSHOT.jar`)

##### Full Command Example: `cd .. & mvn clean install -DskipTests & cd target & java -jar template-0.0.1-SNAPSHOT.jar`
