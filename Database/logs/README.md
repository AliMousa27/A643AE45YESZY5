# Logs Database

This is the schema for the logs database, which is used by the following middleware services:

- [Logs](../../Middleware/logs/README.md) - Creates a logs entity whenever a MQTT message is received under the toothtrek super topic.

## Setup

0. Make sure you have a PostgreSQL server running. You can find the instructions to install it [here](https://www.postgresql.org/download/). We recommend using Postgres 16, as that is the version we used to develop and test the system.
1. Enter the PostgreSQL command line using the command `psql -U <username>`. You will be prompted to enter your password.
2. Create a new database in your PostgreSQL server. You can do this using the command `CREATE DATABASE <database-name>;` in the PostgreSQL command line. Remember to set the same name of the database to all the services that will be using it.
3. Connect to the database you just created using the command `\c <database-name>;`.
4. Create the tables in the database using the command `\i <path-to-file>/schema.sql;`. This will create all the tables.