<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="CSV Parser - Upload" scope="request" />
<c:import url="templates/head.jsp" />
<!DOCTYPE html>
<html lang="en">
<body class="bg-dark">

    <div class="container bg-white p-0">
        <c:import url="templates/header.jsp" />

        <main class="m-3">
            <h1>Upload File</h1>
            <p>In order to use this service, you must upload a CSV file that will be parsed and converted into JSON.</p>
            <p><span class="text-danger">*</span> - Required field</p>

            <form action="upload" method="post" enctype="multipart/form-data">
                <div id="csvFileInputGroup">
                    <p><span class="text-danger">*</span>Upload a Comma-Separated Value (.csv) file</p>
                    <div class="custom-file mb-3">
                        <label id="fileInputLabel" for="fileInput" class="custom-file-label">Choose file</label>
                        <input id="fileInput" type="file" name="file" class="custom-file-input" accept="text/csv" required>
                    </div>
                </div>
                <div id="rawCSVInputGroup" class="form-group" hidden>
                    <label for="csvTextInput"><span class="text-danger">*</span>Paste CSV Code Below</label>
                    <textarea name="csvText" id="csvTextInput" class="form-control" required disabled></textarea>
                </div>
                <div class="form-check pb-3">
                    <input type="checkbox" id="pasteCSVInput" class="form-check-input">
                    <label for="pasteCSVInput">Paste Raw CSV</label>
                </div>


                <input type="hidden" id="fileNameInput" name="fileName" value="">

                <input type="submit" value="Upload" class="btn btn-primary mb-3">
            </form>

            <c:choose>
                <c:when test="${feedback != null}">
                    <p class="text-danger">${feedback}</p>
                </c:when>
            </c:choose>
        </main>
    </div>
</body>
<c:import url="templates/scripts.jsp"></c:import>
</html>
