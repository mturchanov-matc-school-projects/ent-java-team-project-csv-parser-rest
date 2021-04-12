3/23/2021 ~ 1.5 hour 
Tasks Completed:
* readCsvFileWithoutPojo is written  

3/24/2021 ~ 1 hours
Tasks Completed:
* because issues with jersey-endpoints with already existed non-web project  
  new project from scratch was created
* an added setup for view/controller + written TODOs  

3/29/2021 ~ 20 min
Tasks Completed:  
* journal created  
* readme created
* screenshot of parsed claims.csv added as a week9 exercise

4/5/2021 ~ 3 hours
* Add basic CSV file form upload and processing servlet
* Add webapp properties configuration file
* Stylize index page with Bootstrap

4/6/2021 ~ 3 hours
* Add CSV file cleanup method
* Add feedback messages for users based on upload status
* Add page to select endpoint for processing JSON
* Add JavaScript form styling for HTML file input labels

4/7/2021 ~ 7 hours
* Flesh out endpoints.jsp plan
* Implement basic access to API endpoints through endpoints.jsp
* Add JSONQueryService endpoint for formal use
* Add get all query
* Begin implementing get by column value query
* Add basic documentation page

4/8/2021 ~ 1.5 hours
* Met and discussed plans for handling endpoint requests and project design

4/9/2021 ~ 10 hours
* Add JSON querying using Gson
* Experimented with JSON querying system using JsonPath API
* Redesigned endpoints.jsp for clarity and conciseness
* Updated form styling JavaScript to handle new endpoints.jsp design
* Stored parsed JSON into session attribute for processing in GET requests to endpoints
* Removed old Test endpoint
* Add POST endpoints for non-web based JSON filtering by POSTing data in request body
* Add methods to parse and process POST body data and convert it into usable data

4/10/2021 ~ 11 hours
* Add filter options for numbers and values
* Fix logic for numeric comparison operators on right-side of value
* Flesh out design for documentation.jsp
* Create test classes for JsonFilter and CsvUtil classes
* Update POST endpoints to process and filter data in one request
* Test endpoints using curl

4/11/2021 ~ 7 hours
* Finish implementing QueryParamData annotations into JSONQueryService
* Move file handling into CSVFileWriter utility class
* Add more filtering options including OR, NOR, NOT, and REGEX
* Update file error handling on UploadAction servlet
* Update documentation with new endpoints
* Update test classes with new filtering options
* Add feature to paste raw CSV code into input form for parsing
* Test endpoints using curl
* Clean up JSP styling
