<!DOCTYPE html>
<html lang="en">
<head>
    <title>CSV Parser API Documentation</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
</head>
<body class="bg-dark">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<body>
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
                <label for="file" class="custom-file-label">Choose file</label>
                <input id="file" type="file" class="custom-file-input" accept="text/csv" required>
            </div>
            <input type="submit" value="Upload" class="btn btn-primary">
        </form>
    </div>
</body>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>
</html>
