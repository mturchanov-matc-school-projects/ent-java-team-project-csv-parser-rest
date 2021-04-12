<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="request" value="${pageContext.request}"/>
<c:set var="baseUrl" value="${request.scheme}://${request.serverName}:${request.serverPort}${request.contextPath}/"/>
<c:set var="title" value="CSV Parser - Upload" scope="request"/>
<c:set var="styleSheet" value="documentation.css" scope="request"/>
<!DOCTYPE html>
<html lang="en">
<c:import url="templates/head.jsp"/>
<body class="bg-dark">
<button onclick="topFunction()" id="myBtn" title="Go to top">Top</button>

<div class="container bg-white p-0">
    <c:import url="templates/header.jsp" />
    <main class="m-3 pb-3">

        <h1>JSON Query API</h1>

        <div class="card">
            <div class="card-body">
                <p class="card-title h2">Table of contents</p>

                <div id="toc_container">
                    <ol class="toc_list">
                        <li>1. <a href="#getStarted">Get started</a>
                            <ol>
                                <li> 1.1 <a href="#howToGet">GET requests (with view)</a></li>
                                <li> 1.2 <a href="#howToPost">POST requests (without view)</a></li>
                            </ol>
                        </li>
                        <li> 2. <a href="#endpoints">Endpoints</a></li>
                        <li> 3. <a href="#filteringOptions">Form filtering options</a></li>
                    </ol>
                </div>
            </div>
        <div class="card">
            <div class="card-body">
                <p class="card-title h2">Introduction</p>
                <p class="card-text">The JSON Query RESTful API provides the ability to search a given set of JSON data
                    and
                    return all matching JSON elements. </p>
                <p class="card-text">The API expects a list of JSON elements containing the same fields as each other.
                    If an
                    element does not contain a field, that element gets ignored.</p>
                <p class="card-text">The application is a RESTful version of <a href="https://github.com/StateFarmInsCodingCompetition/2020-StateFarm-CodingCompetitionProblem.git">csv-parser problem</a> .</p>
            </div>
        </div>
        <br>
        <div class="card">
            <p class="card-title h3 ml-3" id="getStarted">Quick Start Guide</p>
            <div id="guides" class="ml-3">
                <div class="card-body">

                    <p class="card-title h4" id="howToGet">For GET parsing -- with view </p>

                    <p>For users eager to parse CSV to [filtered] JSON here are a few quick steps to begin:</p>
                    <ol>
                        <li><b>Upload a CSV file</b></li>
                        <ol>
                            <li><b>Read </b><a href="documentation.jsp">documentation</a> if parsed data filtering needed</li>
                            <li><b>Fulfill inputs</b> in a form to generate a filtered JSON from parsed csv-file</li>
                        </ol>
                        <li><b>Receive JSON</b></li>
                    </ol>

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
                </div>
                <div class="card-body">
                    <p class="card-title h4" id="howToPost">For POST parsing -- doesn't require view</p>
                    <p class="card-text">For developers eager to implement this API, here are a few quick steps to parse a
                        CSV
                        file to filtered JSON:</p>

                    <ol class="card-text">
                        <li><b>Use an environment</b> you like to make a POST request</li>
                        <li><b>In POST request</b> specify a CSV file to upload using the <code>&lt;file&gt;</code> form
                            parameter
                        </li>
                        <li><b>Write or specify</b> the <code>&lt;search&gt;</code> criteria form parameter as a JSON object
                            containing each column to filter
                        </li>
                        <li><b>Submit</b> and receive the parsed response</li>
                    </ol>

                    <div class="card">
                        <details class="m-2">
                            <summary class="card-title h5">Making <code>POST</code> requests with curl</summary>
                            <div class="card-body">
                                <p class="card-text">Post CSV file (using <a href="resources/fruits.csv">fruits.csv</a>)</p>
                                <pre class="card-text">curl -X POST -F "file=@fruits.csv" -F 'search={"fruit":"banana","number":30}' "${baseUrl}rest/jsonqueryservice/search"</pre>
                                <p><span class="text-danger">*</span>Subsequent requests must be made through GET and include
                                    session data.</p>
                            </div>
                        </details>
                    </div>


                </div>
            </div>



        </div>

        <div class="card" id="notes">
            <div class="card-body">
                <p class="card-title h4">Important when making requests</p>
                <p>All query parameters on all endpoints are <span class="text-danger">optional!</span></p>
                <p>Search query parameters <span class="text-danger">must</span> match the CSV column name and have
                    appropriate
                    type value.
                </p>
            </div>
        </div>


        <div class="card">
            <h2 class="card-title h2" id="endpoints">Endpoints</h2>
            <div class="card">
                <div class="card-body">
                    <p class="card-title h4">Send all data requests to: </p>
                    <pre>${baseUrl}<span class="text-danger">rest/jsonqueryservice/</span></pre>
                </div>
            </div>
            <div class="card-body">
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
                        <td>Returns JSON from parsed CSV file. If parameter(s) are entered, then returns an array of filtered
                            JSON
                        </td>
                        <td>In order to query using a GET request, a POST request <span class="text-danger">must</span> first be
                            made to ${baseUrl}upload containing the CSV <code>&lt;file&gt;</code> or raw
                            <code>&lt;csvText&gt;</code> parameters.
                            The POST session data <span class="text-danger">must</span> be retained and provided for all
                            subsequent
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
                            made to ${baseUrl}upload containing the CSV <code>&lt;file&gt;</code> or raw
                            <code>&lt;csvText&gt;</code> parameters.
                            The POST session data <span class="text-danger">must</span> be retained and provided for all
                            subsequent
                            GET requests.
                        </td>
                    </tr>
                    <tr>
                        <td><span class="text-danger">/search</span></td>
                        <td>POST</td>
                        <td><code>&lt;file&gt;</code></td>
                        <td><code>&lt;search&gt;</code></td>
                        <td>Returns JSON from the provided CSV file. If a JSON object search parameter is entered, then returns
                            an array of filtered JSON
                        </td>
                        <td>Query parameters <span class="text-danger">must</span> match the CSV column name and have
                            appropriate
                            type value. Subsequent GET requests can be made if session data is provided.
                        </td>
                    </tr>
                    <tr>
                        <td><span class="text-danger">/count</span></td>
                        <td>POST</td>
                        <td><code>&lt;file&gt;</code></td>
                        <td><code>&lt;search&gt;</code></td>
                        <td>Returns count of items. If a JSON object search parameter is entered, then returns an array of
                            filtered JSON
                        </td>
                        <td>Query parameters <span class="text-danger">must</span> match the CSV column name and have
                            appropriate
                            type value. Subsequent GET requests can be made if session data is provided.
                        </td>
                    </tr>
                </table>
            </div>

        <br>

        <div class="card">
            <h2 class="card-title h2" id="filteringOptions">Filtering Options</h2>
                <div class="card-body">
                    <p class="card-text">Aside from comparing exact values, there are a variety of additional comparison
                        and
                        filtering operators available for use. Note that operators cannot be mixed in a single query
                        parameter.
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
                        <tr>
                            <td>JSON Object Filtering</td>
                            <td>Filter values that contain certain JSON object data using jsonPath</td>
                            <td><code>$...</code></td>
                            <td><code>$..[?(@.firstName== 'Trudie')]</code></td>
                            <td>See documentation for <a href="https://github.com/json-path/JsonPath">JsonPath</a></td>
                        </tr>
                    </table>
                </div>
        </div>


    </main>

</div>
<script src="js/scrollToTop.js"></script>

</body>
<c:import url="templates/scripts.jsp"></c:import>
</html>
