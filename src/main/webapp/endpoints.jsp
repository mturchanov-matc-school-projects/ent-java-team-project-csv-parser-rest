<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<c:set var="title" value="CSV Parser - Filter" scope="request"></c:set>
<c:import url="templates/head.jsp"></c:import>
<body class="bg-dark">
    <div class="container bg-white p-3">
        <h1>View Converted JSON</h1>
        <p>Select a query option to run on the JSON.</p>

        <c:choose>
            <c:when test="${feedback != null}">
                <p class="text-success">${feedback}</p>
            </c:when>
        </c:choose>

        <form id="queryForm" action="" method="get">
            <div class="form-group">
                <label for="jsonTextArea">Parsed JSON</label>
                <textarea id="jsonTextArea" class="form-control" readonly>${json}</textarea>
            </div>

            <div class="form-group">
                <label for="queryTypeInput">JSON Filter</label>
                <select id="queryTypeInput" class="form-control">
                    <option value="count">Get Count</option>
                    <option value="search">Search Column(s)</option>
                </select>
            </div>

            <div id="queryColumnGroup">
                <p>Enter Values to Search</p>

                <c:forEach var="column" items="${columns}">
                    <div class="form-group">
                        <label for="${column}">${column}</label>
                        <input type="text" id="${column}" name="${column}" class="form-control">
                    </div>
                </c:forEach>
            </div>

            <input type="submit" class="btn btn-primary">
        </form>
    </div>
</body>
<c:import url="templates/scripts.jsp"></c:import>
</html>
