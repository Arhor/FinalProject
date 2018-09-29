$(document).ready(function () {

    // var enrolleeID = $('#enrolleeID').val();

    // if (enrolleeID > 0) {

        var sendTo = {
            command    : 'check_users',
            userId  : []
        };

        $('.admission-user').each(function () {
            sendTo.userId.push($(this).attr('id'));
        });

        $.ajax({
            url: 'ajaxServlet',
            data: sendTo,
            dataType: 'text json',
            success : function (data) {

                $('.admission-user').each(function () {
                    var uid = $(this).attr('id');
                    var result = data['resultSet'][uid];
                    switch (result.toString()) {
                        case 'false':
                            $('#uid' + uid + '.btn-danger').css('display', 'inline');
                            break;
                        case 'true':
                            $('#uid' + uid + '.btn-success').css('display', 'inline');
                            break;
                    }
                });

            }
        });

    // } else {
    //     $('.success').css('visibility', 'hidden').attr('disabled', 'disabled');
    //     $('.danger').css('visibility', 'hidden').attr('disabled', 'disabled'); // REFACTORING AWAITS YOU *_*
    // }

});

$('.btn-success').click(function () {
    var sendTo = {
        command : 'block_user',
        userId  : $(this).attr('id')
    };
    $.ajax({
        url: 'ajaxServlet',
        data: sendTo,
        dataType: 'text json',
        success : function (data) {
            switch (data['result'].toString()) {
                case 'true':
                    $('#uid' + data['userId'].toString() + '.btn-danger').css('display', 'inline');
                    $('#uid' + data['userId'].toString() + '.btn-success').css('display', 'none');
                    break;
                case 'false':
                    // TODO: implement false user blockhandling
                    break;
            }
        }
    });
});

$('.btn-danger').click(function () {
    var sendTo = {
        command : 'unblock_user',
        userId  : $(this).attr('id')
    };
    $.ajax({
        url: 'ajaxServlet',
        data: sendTo,
        dataType: 'text json',
        success : function (data) {
            switch (data['result'].toString()) {
                case 'true':
                    $('#uid' + data['userId'].toString() + '.btn-success').css('display', 'inline');
                    $('#uid' + data['userId'].toString() + '.btn-danger').css('display', 'none');
                    break;
                case 'false':
                    // TODO: implement false user unblock handling
                    break;
            }
        }
    });
});