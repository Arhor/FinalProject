$(document).ready(function () {

    var enrolleeID = $('#enrolleeID').val();

    if (enrolleeID > 0) {

        var sendTo = {
            command    : 'check_faculty',
            subjectId  : [],
            enrolleeId : $('#enrolleeID').val(),
            facultyId  : []
        };

        $('.subjectID').each(function () {
            sendTo.subjectId.push($(this).val())
        });

        $('.faculty').each(function () {
            sendTo.facultyId.push($(this).attr('id'));
        });

        $.ajax({
            url: 'ajaxServlet',
            data: sendTo,
            dataType: 'text json',
            success : function (data) {

                console.log(data['resultSet']);

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
        });

    } else {
        $('.success').css('visibility', 'hidden').attr('disabled', 'disabled');
        $('.danger').css('visibility', 'hidden').attr('disabled', 'disabled'); // REFACTORING AWAITS YOU *_*
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
            switch (data['result'].toString()) {
                case 'true':
                    $('#fac' + data['faculty'].toString() + '.btn-danger').css('display', 'inline');
                    $('#fac' + data['faculty'].toString() + '.btn-success').css('display', 'none');
                    break;
                case 'false':
                    // TODO: implement false faculty registration handling
                    break;
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
            switch (data['result'].toString()) {
                case 'true':
                    $('#fac' + data['faculty'].toString() + '.btn-success').css('display', 'inline');
                    $('#fac' + data['faculty'].toString() + '.btn-danger').css('display', 'none');
                    break;
                case 'false':
                    // TODO: implement false faculty registration handling
                    break;
            }
        }
    });
});