function startTimer(duration, display) {
    var timer = duration, minutes, seconds;
    --timer;
    var session = setInterval(function () {
        minutes = parseInt(timer / 60, 10);
        seconds = parseInt(timer % 60, 10);
        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;
        display.textContent = minutes + ":" + seconds;
        if (--timer < 0) {
            clearInterval(session);
            $('#sessionExpired').modal({
                backdrop : 'static',
                keyboard : false
            });
        }
    }, 1000);
}

window.onload = function () {
    var time = document.getElementById("maxInactivity").getAttribute("value");
    var display = document.querySelector('#time');
    startTimer(time, display);
};

$('#sessionExpired').on('hidden.bs.modal', function () {
    window.location.replace("/controller?command=start");
});