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
    <div class="container bg-white">
        <h1>CSV Parser API Documentation</h1>
        <p>To use the CSV parser API, there are several handy features available to load data.</p>
        <p>While uploading personal CSV files is not currently available, you can access the following data on a
            pre-made <a href="files/claims.csv">clams.csv</a> file.</p>

        <section>
            <div class="card">
                <div class="card-body">
                    <p class="card-title h3">All Claims</p>
                    <p class="card-text small">/rest/parsed_items</p>
                    <p class="card-text">Returns a JSON array of all claims.</p>
                </div>
            </div>
        </section>
    </div>
</body>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>
</html>
