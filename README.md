# RESTful CSV Parser API

### Problem Statement

CSV or comma-separated values files are a common way to exchange large amounts of data between different applications. 
In their raw state (as viewed from a text editor) they can seem messy. To solve this the file must be parsed! This 
application aims to provide a service where the user can upload any CSV file and have it parsed into JSON. From there 
the user can run a number of queries against their newly parsed data. In addition to the web based UI potential users 
can also access the parsing and query functionality through a Post request!

Originally this project was based on the [State Farm 2020 Coding Competition](OriginalProblemStatement.md) but was
heavily modified to meet this projects specifications.

### Project Objectives

* Create a RESTful service.
* Create a web application to house it.  
* Parse incoming CSV file into JSON from both the web application and POST requests.
* Allow a wide range of querys to be preformed on the newly formatted data sets. 
* Provide documentation on all services to ensure ease of use for end user.


### Presentation Description/Overview
#### Description
The app is a rest CSV parser to JSON format. 
In addition, the application provides basic filtering of JSON based on user's input


#### Pages and functionality:
###### Index Page
* Upload csv page

###### Documentation Page
* Documentation page with endpoints

###### Endpoints
* GET and POST at .../rest/jsonqueryservice/search
* GET and POST at .../rest/jsonqueryservice/count


###### Logic on rest
Upload CSV file for parsing application to generate JSON containing all CSV data.   
Endpoints will check query parameters to filter JSON with all items.

These steps can be done in one single step if CSV data is POSTed to either of the endpoints with a search query.

### Project Design Documents
* [Link to the project plan](ProjectPlan.md)
* [Link to the journal](Journal.md)