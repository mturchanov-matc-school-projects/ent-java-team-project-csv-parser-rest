<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<c:set var="title" value="CSV Parser - Upload" scope="request"></c:set>
<c:import url="templates/head.jsp"></c:import>
<body class="bg-dark">

    <div class="container bg-white p-3">
        <h1>JSON Query API</h1>
        <p>The JSON Query RESTful API provides the ability to search a given set of JSON data and return all matching
        JSON elements. The API expects a list of JSON elements containing the same fields as each other. If an element
        does not contain a field, that element gets ignored.</p>

        <p>The JSON Query API can be accessed at http://localhost:8080/csvparser/rest/query</p>

        <p>In order to query through the JSON data, there are several parameters available that must be included in the
        POST request in order to complete the request.</p>

        <ul>
            <li><span class="small">queryType</span> - The type of query being performed, which can include</li>
        </ul>

        <div class="card">
            <div class="card-body">
                <p class="card-title h3">Get All JSON Elements</p>
                <p class="card-text">Returns every JSON element provided without performing any sort of querying.</p>
                <p class="card-text">Parameters:</p>
            </div>
        </div>


    </div>
</body>
<c:import url="templates/scripts.jsp"></c:import>
</html>
