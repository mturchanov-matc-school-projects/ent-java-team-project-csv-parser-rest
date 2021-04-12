<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="CSV Parser - Filter" scope="request" />
<c:set var="styleSheet" value="endpoints.css" scope="request" />
<!DOCTYPE html>
<html lang="en">
<c:import url="templates/head.jsp" />
<body class="bg-dark">

<button onclick="topFunction()" id="myBtn" title="Go to top">Top</button>
    <div class="container bg-white p-0">
        <c:import url="templates/header.jsp" />

        <main class="m-3 pb-3">
            <h1>View Converted JSON</h1>
            <p>Select a query option to run on the JSON.</p>

            <c:choose>
                <c:when test="${feedback != null}">
                    <p class="text-success">${feedback}</p>
                </c:when>
            </c:choose>

            <form id="queryForm" action="rest/jsonqueryservice/search" method="get">
                <p>Enter Values to Search</p>

                <c:forEach var="column" items="${columns}">
                    <div class="form-group">
                        <label for="${column}">${column}</label>
                        <input type="text" id="${column}" name="${column}" class="form-control">
                    </div>
                </c:forEach>

                <div class="form-check pb-3">
                    <input id="countResults" type="checkbox" class="form-check-input">
                    <label for="countResults" class="form-check-label">Count Results</label>
                </div>

                <input type="submit" class="btn btn-primary">
            </form>

            <br>
            <hr>

            <div class="card">
                <div class="card-body">
                    <button id="btnJson" class="pointer btn btn-secondary" >Show JSON</button>
                    <textarea id="textarea" class="form-control" readonly>${json}</textarea>
                </div>
            </div>
        </main>

    </div>
</body>

<script src="js/textareaBeautifier.js"></script>
<script src="js/scrollToTop.js"></script>
<c:import url="templates/scripts.jsp"></c:import>

</html>
