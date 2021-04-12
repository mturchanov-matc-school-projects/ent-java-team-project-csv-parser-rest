<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="request" value="${pageContext.request}" />
<c:set var="baseUrl" value="${request.scheme}://${request.serverName}:${request.serverPort}${request.contextPath}/" />
<!DOCTYPE html>
<html lang="en">
<c:set var="title" value="CSV Parser - Upload" scope="request" />
<c:import url="templates/head.jsp" />
<head>
    <style>
        @media (min-width: 576px){
            .container, .container-sm {
                max-width: 85%;
            }
        }
        th, td {
            padding:1em;
        }
        pre {
            background: #eee;
            padding: 1em;
        }
    </style>
</head>
<body class="bg-dark">

<div class="container bg-white p-3">
    <h1>JSON Query API</h1>


    <div class="card">
        <div class="card-body">
            <p class="card-title h3">Introduction</p>
            <p class="card-text">The JSON Query RESTful API provides the ability to search a given set of JSON data and
                return all matching
                JSON elements. </p>
            <p class="card-text">The API expects a list of JSON elements containing the same fields as each other. If an
                element
                does not contain a field, that element gets ignored.</p>
        </div>
    </div>
    <div class="card">
        <div class="card-body">
            <p class="card-title h3">Quick Start Guide</p>
            <p class="card-text">For developers eager to implement this API, here are a few quick steps to parse a CSV
                file to filtered JSON:</p>

            <ol class="card-text">
                <li><b>Use an environment</b> you like to make a POST request</li>
                <li><b>In POST request</b> specify <code>Content-Type</code> as <code>application/json</code></li>
                <li><b>Write or enter</b> to POST body csv-data or JSOn-data</li>
                <li><b>Receive parsed response</b></li>
            </ol>
            <hr>
            <details>
                <summary class="card-title h4">Example with curl</summary>
                <pre class="card-text">curl -i -X POST -H "Content-Type: application/json" -d '[{"fruit":"banana", "number": 30 }, {"fruit":"apple", "number": 15 }, {"fruit":"apple","number": 2 }, {"fruit":"banana","number": 12}]' http://localhost:8080/csvparser/rest/jsonqueryservice/search?fruit=banana</pre>
                <p class="card-title h4">Expected response</p>
                <pre class="card-text" style="background:#eee;">
[
    {
        "fruit":"banana",
        "number": 30
    },
    {
        "fruit":"banana",
        "number": 12
    }
]
</pre>
            </details>
        </div>
    </div>

    <hr>
    <p>For users eager to parse CSV to [filtered] JSON here are a few quick steps to begin:</p>
    <ol>
        <li><b>Upload a CSV file</b></li>
        <li><b>Write needed values</b> in a form filter to generate a filtered JSON from parsed csv-file</li>
        <li><b>Enjoy the result!</b></li>
    </ol>

    <hr>
    <br>
    <h3>Send all data requests to: </h3>
    <pre>${baseUrl}<span class="text-danger">rest/jsonqueryservice/</span></pre>

    <div class="card">
        <div class="card-body">
            All query parameters on all endpoints are <span class="text-danger">optional!</span>
        </div>
    </div>

    <h2>Endpoints</h2>
    <table>
        <tr>
            <th>Endpoint</th>
            <th>Method</th>
            <th>Optional</th>
            <th>Example</th>
            <th>Description</th>
            <th>Note</th>
        </tr>
        <tr>
            <td><span class="text-danger">/search</span></td>
            <td>GET</td>
            <td><span class="text-danger">yes</span></td>
            <td><code>?closed=true&monthsOpen=8</code></td>
            <td>Returns JSON from parsed csv-file. If parameters are entered then returns a filtered JSON</td>
            <td>Query parameter <span class="text-danger">must</span> be same as csv-column name and has appropriate
                type value.
            </td>
        </tr>
        <tr>
            <td><span class="text-danger">/count</span></td>
            <td>GET</td>
            <td><span class="text-danger">yes</span></td>
            <td><code>?closed=true&monthsOpen=8</code></td>
            <td>Returns count of items. If parameter(s) were applied then returns the a number of filtered items</td>
            <td>Query parameter <span class="text-danger">must</span> be same as csv-column name and has appropriate
                type value.
            </td>
        </tr>
    </table>

    <br>
    <hr>

    <div class="card">
        <div class="card-body">
            <p class="card-title h5">Example <code>GET</code> request:</p>
            <pre>http://localhost:8080/csvparser/rest/jsonqueryservic/search?closed=false&monthsOpen=8</pre>
        </div>
    </div>
</div>
</body>
<c:import url="templates/scripts.jsp"></c:import>
</html>
