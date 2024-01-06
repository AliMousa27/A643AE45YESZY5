# Load Visualization Setup Guide

## Prerequisites

Before you begin, make sure you have the following prerequisites installed and added to your system PATH:

- [Grafana](https://grafana.com/)
- [Prometheus](https://prometheus.io/)

## Steps

### 1. Start the system

Ensure that the website and all relevant services are up and running.

### 2. Start Prometheus

Ensure that you are in the \Monitoring directory and run the following:

```
.\prometheus.exe --config.file=prometheus.yml
```

### 3. Start Grafana

Start the Grafana server by navigating to the directory where Grafana is installed. The command might look like this:

```
cd path\to\grafana\bin
``` 

Run the following:

```
grafana.exe server
```

### 4. Configure Grafana

1. In your browser open http://localhost:3000/.

2. Log in using your credentials. (If this is the first time using Grafana you can simply use "admin" for both)

3. Add a new data source, choose prometheus, and add Prometheus HTTP URL. (default is http://localhost:9090)

4. Import a dashboard using the json file include in the folder called "grafana_config.json".

5. Save the dashboard.