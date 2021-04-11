<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<c:set var="title" value="CSV Parser - Upload" scope="request"></c:set>
<c:import url="templates/head.jsp"></c:import>
<body class="bg-dark">

<!-- TODO: documentation on endpoint
     TODO/extra: dynamically form endpoint url. (catch #csvParams in controller send to the view)
              then parse through parameters in a form(taglib) and with js shape the end-point url
-->
    <div class="container bg-white p-3">
        <h1>Upload File</h1>
        <p>In order to use this service, you must upload a CSV file that will be parsed and converted into JSON.</p>
        <p><span class="text-danger">*</span> - Required field</p>

        <form action="upload" method="post" enctype="multipart/form-data">
            <p><span class="text-danger">*</span>Upload a Comma-Separated Value (.csv) file</p>
            <div class="custom-file mb-3">
                <label id="fileInputLabel" for="fileInput" class="custom-file-label">Choose file</label>
                <input id="fileInput" type="file" name="file" class="custom-file-input" accept="text/csv" required>
            </div>
            <div class="form-group">
                <label for="csvTextInput"><span class="text-danger">*</span>Paste CSV Code Below</label>
                <textarea name="csvText" id="csvTextInput" class="form-control"></textarea>
            </div>
            <div class="form-check pb-3">
                <input type="checkbox" id="pasteCSVInput" class="form-check-input">
                <label for="pasteCSVInput">Paste Raw CSV</label>
            </div>
            <input type="submit" value="Upload" class="btn btn-primary">
        </form>

        <c:choose>
            <c:when test="${feedback != null}">
                <p class="text-danger">${feedback}</p>
            </c:when>
        </c:choose>
    </div>
</body>
<c:import url="templates/scripts.jsp"></c:import>
</html>
