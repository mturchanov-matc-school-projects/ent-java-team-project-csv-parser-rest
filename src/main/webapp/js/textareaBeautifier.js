let button = document.getElementById('btnJson');
document.getElementById('textarea').style.display = 'none';
button.onclick = function() {
    var div = document.getElementById('textarea');
    if (div.style.display !== 'none') {
        div.style.display = 'none';
        document.getElementById('btnJson').innerHTML = "Show JSON"

    } else {
        document.getElementById('btnJson').innerHTML = "Hide JSON"
        div.style.display = 'block';
        $(function () {
            $('textarea').each(function () {
                $(this).height($(this).prop('scrollHeight'));
                let textareaEl = document.getElementById("textarea");
                let strJson = textareaEl.value;
                textareaEl.innerHTML = JSON.stringify(JSON.parse(strJson), null, 4)
            });
        });
    }
}