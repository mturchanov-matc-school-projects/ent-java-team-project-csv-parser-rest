# Movies aggregator
### Presentation Description/Overview
#### Description
The app is a rest csv parser to JSON format. 
In addition, the application provides the basic filtering of JSON based on user's input  


#### Pages and functionality:
###### Index
* upload csv page  
* documentation page with endpoints  
    * (maybe) dynamically creating an endpoint based on user input's   
* bunch of rest endpoints  


###### Logic on rest
After parsing csv application generates JSON with all items.   
Endpoints will check query parameters to filter JSON with all items.

Then based on this JSON several endpoints are generated:  
* getAll?param=val&...  
* getById  
* getCount?param=val&...  


### Journal

[Link to the journal](Journal.md)
