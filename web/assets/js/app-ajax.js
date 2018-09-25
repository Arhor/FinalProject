$(document).ready(function () {

    var enrolleeID = $('#enrolleeID').val();

    if (enrolleeID > 0) {

    } else {
        $('.btn').css('visibility', 'hidden').attr('disabled', 'disabled');
    }

});