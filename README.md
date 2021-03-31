# CaSchE

### Project Description
This application provides features for customers, employees and administrators of a store. 

### Technologies Used
* Apache-Maven 3.6.3
* Spring Tool Suite
*Spring Boot Framework 2.4.3
* Docker v0.9.1-beta3
* Kubernetes
* Grafana 6.4.5
* Loki 2.3.0
* Fluentd 0.3.4
* Log4J 1.8.4
* Prometheus
* Jenkins
* Mockito 3.8.0
* JUnit 4.13.2

### Features
This application has login feature where users must enter a valid username and password in order to use the service. Once logged in, it allows customers to perform actions such as view the store's inventory, add an item to their cart, and checkout. Employees can view the directory of all users. Administratos can display all users and their transactions, create, modify and delete coupons, and revise the quantities of items in stock.

### Getting Started
* the command to clone the repository is 'git clone https://github.com/210201sre/CaSchE.git'
* If you have access to the Revature SRE cluster, use the command 'kubectl get ing'
* Under the 'ADDRESS' field, copy the url 'a62d60162057149549617360016d2e38-542496291.us-east-1.elb.amazonaws.com' and add :8080 to the end to specify the port. Use this address to send http requests to the application.


### Usage

In order to use the application, you need to be logged in. You can log in by sending a post request to a62d60162057149549617360016d2e38-542496291.us-east-1.elb.amazonaws.com:8080/isms. The body of the request would have the username and password in json form:
    {
        "uname": "password",
        "pswrd": "password"
    }

After you have logged in, you can 


### Contributors
* Jonathan Schmitz
* Hugo Espejel
* Ben Cady