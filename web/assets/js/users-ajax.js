$(document).ready(function () {
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
            if (data['error']) {
                window.location.replace('/jsp/error.jsp');
            } else {
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
        }
    });
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
            if (data['error']) {
                window.location.replace('/jsp/error.jsp');
            } else {
                switch (data['result'].toString()) {
                    case 'true':
                        $('#uid' + data['userId'].toString() + '.btn-danger').css('display', 'inline');
                        $('#uid' + data['userId'].toString() + '.btn-success').css('display', 'none');
                        break;
                    case 'false':
                        // TODO: implement false user block handling
                        break;
                }
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
            if (data['error']) {
                window.location.replace('/jsp/error.jsp');
            } else {
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
        }
    });
});