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
* Mockito

### Features
This application has login feature where users must enter a valid username and password in order to use the service. Once logged in, it allows customers to perform actions such as view the store's inventory, add an item to their cart, and checkout. Employees can hire customers release employees.

### Getting Started
* the command to clone the repository is 'git clone https://github.com/210201sre/CaSchE.git'
* If you have access to the Revature SRE cluster, use the command 'kubectl get ing'
* Under the 'ADDRESS' field, copy the url 'a62d60162057149549617360016d2e38-542496291.us-east-1.elb.amazonaws.com' and add :8080 to the end to specify the port. Use this address to send http requests to the application.


### Usage

The features involving students include:

* GET /students -finds all the students in the roster.
* GET /students/{n} -finds information on a particular student. The student's name, n, is entered in the URI.
* POST /students -adds a student to the roster: the user enters JSON in the request body of the format {"name":"John Johnson", 
email:"jjohnson@mail.com", "gpa": 3.5}
* DELETE: /students/{n} -removes a student from the roster based on the name, n, given in the URI.
* PUT: /students/status -updates the academic status of all students in the roster. All students have a field indicating whether or not they are on academic probation, which is initially set to 'false' for everyone in the roster. This request changes this status to 'true' for all students with a gpa less than 2.0.
* GET: /students/gpa/{category} -this finds the average gpa of students based on the category given in the URI. The category is either 'all' or 'some'. /students/gpa/all calculates the average gpa of all students in the roster, while /students/gpa/some calculates the gpa of the students who are not on academic probation.
* DELETE: /truncate -removes all students from the roster

The features involving the courses include:
* GET /courses -finds all the courses in the course catalog.
* POST /courses - adds a course to the course catalog. The user enters data as a JSON in the request body, in the format {"name":"Calculus", "professor": "Gauss", "building: "Olin"}.
* DELETE /courses/{name} -removes a course by the name given in the URI.
* GET /courses/Building/{building} -finds all the courses in the building given in the URI.

To-Do List:
* Create an endpoint so a student can be enrolled in a course
* Create a join table so a many-to-many relationship can be implemented. This would allow users to find all the courses taken by a particular student, and all the students enrolled in a particular course.

###Contributors
* Jonathan Schmitz
* Hugo Espejel
* Ben Cady