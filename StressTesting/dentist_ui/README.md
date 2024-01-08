# Dentist UI Backend

This is the ```locustfile``` script for running the stress test for the following components:

- [Dentist UI Express Server](../../DentistUI/README.md) - The backend server for the dentist UI, which is responsible for handling the requests from the dentist UI and communicating to the middleware through the broker.

## Setup

0. Run the Express server for the website backend. You can find the instructions to do so [here](../../Website/Backend/README.md).
1. Open a terminal and navigate to ```cd /../StressTesting/dentist_ui/```
2. Run ```locust``` to start the Locust server.
3. Open a browser and go to ```http://localhost:8089/```
4. Enter values for the following fields:
    - Number of total users to simulate
    - Hatch rate (users spawned/second)
    - Host (http://localhost:3000)
5. Click on ```Start swarming```
6. Done!
