<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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

        <form id="queryForm" action="" method="post">
            <div class="form-group">
                <label for="queryTypeInput">Search for Item</label>
                <select name="queryType" id="queryTypeInput" class="form-control">
                    <option value="all">Get All Items</option>
                    <option value="value">Get Item(s) with Column Value</option>
                </select>
            </div>

            <div id="queryColumnGroup">
                <div class="form-group">
                    <label for="queryColumnInput">Search in Column</label>
                    <select name="queryColumn" id="queryColumnInput" class="form-control">
                        <c:forEach var="column" items="${columns}">
                            <option value="${column}">${column}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="queryColumnValue">Search for Value</label>
                    <input id="queryColumnValue" name="queryColumnValue" type="text" class="form-control">
                </div>
            </div>

            <input type="hidden" name="json" value="${fn:escapeXml(json)}">

            <input type="submit" class="btn btn-primary">

            <div class="form-group">
                <label for="jsonTextArea">Raw JSON</label>
                <textarea id="jsonTextArea" class="form-control" readonly>${json}</textarea>
            </div>
        </form>
    </div>
</body>
<c:import url="templates/scripts.jsp"></c:import>
</html>
