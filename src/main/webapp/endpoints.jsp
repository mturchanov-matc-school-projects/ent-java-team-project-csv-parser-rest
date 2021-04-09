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

        <form id="queryForm" action="${pageContext.request.contextPath}/rest/jsonqueryservice/search" method="get">
            <c:forEach var="column" items="${columns}">
                <label for="${column}">${column}</label>
                <input type="text" name="${column}" id="${column}"> <br>
            </c:forEach>
            <br>
            <input type="submit" value="Ok">
        </form>
    </div>
</body>
<c:import url="templates/scripts.jsp"></c:import>
</html>
