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
            background: #eeeeee;
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
                return all matching JSON elements. </p>
            <p class="card-text">The API expects a list of JSON elements containing the same fields as each other. If an
                element does not contain a field, that element gets ignored.</p>
        </div>
    </div>
    <div class="card">
        <div class="card-body">
            <p class="card-title h3">Quick Start Guide</p>
            <p class="card-text">For developers eager to implement this API, here are a few quick steps to parse a CSV
                file to filtered JSON:</p>

            <ol class="card-text">
                <li><b>Use an environment</b> you like to make a POST request</li>
                <li><b>In POST request</b> specify a CSV file to upload using the <code>&lt;file&gt;</code> form parameter</li>
                <li><b>Write or specify</b> the <code>&lt;search&gt;</code> criteria form parameter as a JSON object containing each column to filter</li>
                <li><b>Submit</b> and receive the parsed response</li>
            </ol>
            <hr>
            <details>
                <summary class="card-title h4">Example with curl</summary>
                <p>Using a sample file called <a href="resources/fruits.csv">fruits.csv</a>...</p>
                <pre class="card-text">curl -X POST -F "file=@fruits.csv" -F 'search={"fruit":"banana"}' "${baseUrl}rest/jsonqueryservice/search"</pre>
                <p class="card-title h4">Expected response</p>
                <pre class="card-text" style="background:#eee;">
[
    {
        "fruit":"banana",
        "count":30
    },
    {
        "fruit":"banana",
        "count":23
    }
]
</pre>
                <p class="card-title h4">Explanation</p>
                <p class="card-text">The POST request passes the <code>fruits.csv</code> file as well as a search
                    criteria JSON object consisting of each column to search in. Since there are two JSON objects
                    that have a <code>fruit</code> value of <code>banana</code>, two JSON objects are returned.</p>
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
            <p>All query parameters on all endpoints are <span class="text-danger">optional!</span></p>
            <p>Search query parameters <span class="text-danger">must</span> match the CSV column name and have appropriate
                type value.
            </p>
        </div>
    </div>

    <h2>Endpoints</h2>
    <table>
        <tr>
            <th>Endpoint</th>
            <th>Method</th>
            <th>Required Parameters</th>
            <th>Optional Parameters</th>
            <th>Description</th>
            <th>Note</th>
        </tr>
        <tr>
            <td><span class="text-danger">/search</span></td>
            <td>GET</td>
            <td><code>None</code></td>
            <td><code>Can vary</code></td>
            <td>Returns JSON from parsed CSV file. If parameter(s) are entered, then returns an array of filtered JSON</td>
            <td>In order to query using a GET request, a POST request <span class="text-danger">must</span> first be
                made to ${baseUrl}upload containing the CSV <code>&lt;file&gt;</code> or raw <code>&lt;csvText&gt;</code> parameters.
                The POST session data <span class="text-danger">must</span> be retained and provided for all subsequent
                GET requests.
            </td>
        </tr>
        <tr>
            <td><span class="text-danger">/count</span></td>
            <td>GET</td>
            <td><code>None</code></td>
            <td><code>Can vary</code></td>
            <td>Returns count of items. If parameter(s) were applied, then returns the number of filtered items</td>
            <td>In order to query using a GET request, a POST request <span class="text-danger">must</span> first be
                made to ${baseUrl}upload containing the CSV <code>&lt;file&gt;</code> or raw <code>&lt;csvText&gt;</code> parameters.
                The POST session data <span class="text-danger">must</span> be retained and provided for all subsequent
                GET requests.
            </td>
        </tr>
        <tr>
            <td><span class="text-danger">/search</span></td>
            <td>POST</td>
            <td><code>&lt;file&gt;</code></td>
            <td><code>&lt;search&gt;</code></td>
            <td>Returns JSON from the provided CSV file. If a JSON object search parameter is entered, then returns an array of filtered JSON</td>
            <td>Query parameters <span class="text-danger">must</span> match the CSV column name and have appropriate
                type value. Subsequent GET requests can be made if session data is provided.
            </td>
        </tr>
        <tr>
            <td><span class="text-danger">/count</span></td>
            <td>POST</td>
            <td><code>&lt;file&gt;</code></td>
            <td><code>&lt;search&gt;</code></td>
            <td>Returns count of items. If a JSON object search parameter is entered, then returns an array of filtered JSON</td>
            <td>Query parameters <span class="text-danger">must</span> match the CSV column name and have appropriate
                type value. Subsequent GET requests can be made if session data is provided.
            </td>
        </tr>
    </table>

    <br>
    <hr>

    <div class="card">
        <details class="m-2">
            <summary class="card-title h5">Additional Filtering Options</summary>
            <div class="card-body">
                <p class="card-text">Aside from comparing exact values, there are a variety of additional comparison and
                    filtering operators available for use. Note that operators cannot be mixed in a single query parameter.
                </p>
                <table>
                    <tr>
                        <th>Operator</th>
                        <th>Description</th>
                        <th>Syntax</th>
                        <th>Example</th>
                        <th>Explanation</th>
                    </tr>
                    <tr>
                        <td>NOT</td>
                        <td>Filter out all values that do not match specified value.</td>
                        <td><code>!...</code></td>
                        <td><code>?fruit=!banana</code></td>
                        <td>Get all fruits that are not a banana.</td>
                    </tr>
                    <tr>
                        <td>OR</td>
                        <td>Filter all values that match at least one query value.</td>
                        <td><code>...|...</code></td>
                        <td><code>?fruit=banana|apple</code></td>
                        <td>Get all bananas or apples.</td>
                    </tr>
                    <tr>
                        <td>NOR</td>
                        <td>Filter out all of the specified values.</td>
                        <td><code>!...|...</code></td>
                        <td><code>?fruit=!banana|apple|orange</code></td>
                        <td>Get all fruits that are not a banana, apple, or orange.</td>
                    </tr>
                    <tr>
                        <td>Numerical Comparison</td>
                        <td>Filter out all numeric values that do not compare in amount.</td>
                        <td><code>[<|>|<=|>=]...</code></td>
                        <td><code>?fruit=banana&count=>25</code></td>
                        <td>Get all bananas that count more than 25.</td>
                    </tr>
                    <tr>
                        <td>REGEX Comparison</td>
                        <td>Filter in all values that match the provided REGEX.</td>
                        <td><code>/.../</code></td>
                        <td><code>?fruit=/^p.+/</code></td>
                        <td>Get all fruits that start with a 'p'.</td>
                    </tr>
                </table>
            </div>
        </details>
    </div>

    <div class="card">
        <details class="m-2">
            <summary class="card-title h5">Making <code>GET</code> requests with curl</summary>
            <div class="card-body">
                <ol>
                    <li class="card-text">
                        Upload CSV file and store session data (using <a href="resources/fruits.csv">fruits.csv</a>)
                        <br>
                        <pre>curl -X POST -c cookies.txt -F "file=@fruits.csv" -o output.txt "${baseUrl}upload"</pre>
                    </li>
                    <li class="card-text">
                        Make request with session data included
                        <pre>curl -X GET -b cookies.txt -o output.txt "${baseUrl}rest/jsonqueryservice/search?fruit=banana"</pre>
                    </li>
                </ol>
            </div>
        </details>
    </div>

    <div class="card">
        <details class="m-2">
            <summary class="card-title h5">Making <code>POST</code> requests with curl</summary>
            <div class="card-body">
                <p class="card-text">Post CSV file (using <a href="resources/fruits.csv">fruits.csv</a>)</p>
                <pre class="card-text">curl -X POST -F "file=@fruits.csv" -F 'search={"fruit":"banana","number":30}' "${baseUrl}rest/jsonqueryservice/search"</pre>
                <p><span class="text-danger">*</span>Subsequent requests must be made through GET and include session data.</p>
            </div>
        </details>
    </div>

</div>
</body>
<c:import url="templates/scripts.jsp"></c:import>
</html>
