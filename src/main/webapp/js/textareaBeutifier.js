$( "#textarea" ).hide();
$(function() {
    $('#btnJson').click(function() {
        $( "#textarea" ).show();
        $(function() {
            $('textarea').each(function() {
                $(this).height($(this).prop('scrollHeight'));
                let textareaEl = document.getElementById("textarea");
                let strJson = textareaEl.value;
                textareaEl.innerHTML = JSON.stringify(JSON.parse(strJson), null, 4)
            });
        });
    });
});