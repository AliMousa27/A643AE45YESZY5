# Stress Testing

Under this folder, you will find sub-folders containing the ```locustfile``` scripts for running the stress test for the different Express servers used by the system. There will also be a brief README file explaining how to run the stress test and general information about it.

## General Setup

For the general setup of the stress testing tool, you will need to have Python and Pip installed. You can find the instructions to install them [here (Python)](https://www.python.org/downloads/) and [here (Pip)](https://pip.pypa.io/en/stable/installation/). The system was developed and tested using Python v3.9.13 and Pip v23.3.2.

When you have Python and Pip installed, you will need to install the dependencies for the stress testing tool. To do so, you can run the following command:

```pip install locust```

We used version 2.20.1 of the Locust library.