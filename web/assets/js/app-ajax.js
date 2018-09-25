$(document).ready(function () {

    var enrolleeID = $('#enrolleeID').val();

    if (enrolleeID > 0) {
        $('.faculty').each(function () {

            var sendTo = {
                command    : 'check_faculty',
                enrolleeId : $('#enrolleeID').val(),
                facultyId  : $(this).attr('id')
            };

            $.ajax({
                url: 'ajaxServlet',
                data: sendTo,
                dataType: 'text json',
                success : function (data) {

                    console.log()

                    switch (data['result'].toString()) {
                        case 'true':
                            $('#' + data['faculty'].toString()).find('.success').css('visibility', 'hidden').attr('hidden', 'hidden');
                            break;
                        case 'false':
                            $('#' + data['faculty'].toString()).find('.danger').css('visibility', 'hidden').attr('hidden', 'hidden');
                            break;
                    }
                }
            });
        });
    } else {
        $('.success').css('visibility', 'hidden').attr('disabled', 'disabled');
        $('.danger').css('visibility', 'hidden').attr('disabled', 'disabled');
    }

});