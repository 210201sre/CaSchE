# CaSchE

### Project Description
This application provides features for customers, employees and administrators of a store. 

### Technologies Used
* Apache-Maven 3.6.3
* Spring Tool Suite
* Spring Boot Framework 2.4.3
* Docker v0.9.1-beta3
* Kubernetes 1.18.9
* Grafana 6.4.5
* Loki 2.3.0
* Fluentd 0.3.4
* Log4J 1.8.4
* Prometheus
* Jenkins 2.277.1
* Mockito 3.8.0
* JUnit 4.13.2

### Features
##### Currently Available:
This application has login feature where users must enter a valid username and password in order to use the service. Once logged in, it allows customers to perform actions such as view the store's inventory, add an item to their cart, and checkout. Employees can view the directory of all users. Administratos can display all users and their transactions, create, modify and delete coupons, and revise the quantities of items in stock.

##### ToDo List:
- Restore & provide testing for features that were removed.
- Implement entity relationships between models.
- Restore access level based security.


### Getting Started
* The command to clone the repository is 'git clone https://github.com/210201sre/CaSchE.git'.
* If you have access to the Revature SRE cluster, use the command 'kubectl get ing'.
* Under the 'ADDRESS' field, copy the url 'a62d60162057149549617360016d2e38-542496291.us-east-1.elb.amazonaws.com' and add :8080 to the end to specify the port. Use this address to send http requests to the application.

Alternatively, if you are setting this up using your own EKS and RDS, then perform the following actions.
* Install / set up helm.
* Using the files from [the previous repository's](https://github.com/210201sre/JonathanSchmitz-p1/tree/main/Project1/kubernetes) loki and grafana charts, do the following.
  * Add ingress-nginx, https://kubernetes.github.io/ingress-nginx and grafana, https://grafana.github.io/helm-charts to helm repo.
  * Run helm install ingress-nginx ingress-nginx/ingress/ngninx;
  * Run helm install grafana grafana/grafana -f grafanavalues.yml;
  * Run helm install loki grafana/loki -f lokivalues.yml;
* Install prometheus and jenkins using a helm chart like in the example below.
```sh
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install my-release bitnami/kube-prometheus
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install my-release bitnami/jenkins
```
* Create environment variables for DB_URL, DB_PASSWORD, and DB_USERNAME.
* After creating a namespace called 'casche', run the deploy.sh script while inside the kubernetes folder to deploy ISMS.
* You must create a github repository and link it to sonarcloud and jenkins, and configure the dockerhub registry to make use of the Jenkinsfile pipeline.

### Usage

In order to use the application, you need to be logged in. You can log in by sending a POST request to a62d60162057149549617360016d2e38-542496291.us-east-1.elb.amazonaws.com:8080/isms. The body of the request would have the username and password in json form:
```JSON
    {
        "uname": "password",
        "pswrd": "password"
    }
```

After you have logged in, you can make a variety of requests based on the type of account you have logged in as. POST and DELETE requests will expect data in JSON format. Please reference the outputs of GET requests to structure the information you plan to send.
A customer account can change user account information, add items to a cart, checkout with the items in the cart, and view transaction and backorders. If the quantity of an item in the cart is greater than that of the current stock, the item will be placed as a backorder. These are located at the following endpoints:
- \[GET, PATCH, DELETE]  /user - view, update, & delete the currently logged in user account
- \[POST, GET, DELETE]  /user/cart - add, view, & delete cart items
- \[PUT]  /user/checkout - perform a checkout action
- \[GET]  /user/transaction/contents - view transactions
- \[GET]  /user/backorder - view backorders

An employee currently has the ability to view the employee directory using the following endpoint:
- \[GET]  /staff/directory

Admin accounts are able to view, add, and delete user accounts and coupons. They also have the ability to delete user transactions as well.
- \[POST, GET, DELETE]  /manager/user - add, view, & delete user account
- \[DELETE]  /manager/user/transaction - delete transaction
- \[POST, GET, PATCH, DELETE]  /manager/coupon - add, view, update, & delete coupon


### Contributors
* Jonathan Schmitz
* Hugo Espejel
* Ben Cady
