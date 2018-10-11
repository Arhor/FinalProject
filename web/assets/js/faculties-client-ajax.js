$(document).ready(function () {

    var enrolleeID = $('#enrolleeID').val();

    if (enrolleeID > 0) {

        var sendTo = {
            command    : 'check_faculties',
            facultyId  : []
        };

        $('.faculty').each(function () {
            sendTo.facultyId.push($(this).attr('id'));
        });

        $.ajax({
            url: 'ajaxServlet',
            data: sendTo,
            dataType: 'text json',
            success : function (data) {
                if (data['error']) {
                    window.location.replace('/jsp/error.jsp');
                } else {
                    $('.faculty').each(function () {
                        var fid = $(this).attr('id');
                        var result = data['resultSet'][fid];
                        console.log(result);
                        if (result !== undefined) {
                            switch (result.toString()) {
                                case 'true':
                                    $('#fac' + fid + '.btn-danger').css('display', 'inline');
                                    break;
                                case 'false':
                                    $('#fac' + fid + '.btn-success').css('display', 'inline');
                                    break;
                            }
                        }
                    });
                }
            }
        });

    } else {
        $('.success').css('visibility', 'hidden').attr('disabled', 'disabled');
        $('.danger').css('visibility', 'hidden').attr('disabled', 'disabled');
    }

});

$('.btn-success').click(function () {
    var sendTo = {
        command : 'register_to_faculty',
        enrolleeId : $('#enrolleeID').val(),
        facultyId  : $(this).attr('id')
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
                        $('#fac' + data['faculty'].toString() + '.btn-danger').css('display', 'inline');
                        $('#fac' + data['faculty'].toString() + '.btn-success').css('display', 'none');
                        break;
                    case 'false':
                        $('#errorMessage').text(data['message'].toString());
                        $('#facultyClosed').modal({
                            backdrop : 'static',
                            keyboard : true
                        });
                        break;
                }
            }
        }
    });
});

$('.btn-danger').click(function () {
    var sendTo = {
        command : 'deregister_from_faculty',
        enrolleeId : $('#enrolleeID').val(),
        facultyId  : $(this).attr('id')
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
                        $('#fac' + data['faculty'].toString() + '.btn-success').css('display', 'inline');
                        $('#fac' + data['faculty'].toString() + '.btn-danger').css('display', 'none');
                        break;
                    case 'false':
                        $('#errorMessage').text(data['message'].toString());
                        $('#facultyClosed').modal({
                            backdrop : 'static',
                            keyboard : true
                        });
                        break;
                }
            }
        }
    });
});