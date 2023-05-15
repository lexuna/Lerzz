
var deck = /*[[${deck}]]*/ null;
var socket = new SockJS('/editDeck');
var stompClient = Stomp.over(socket);
stompClient.connect({}, function(frame) {
    console.log('Verbunden: ' + frame);
});
function sendData() {
    var description = document.getElementById('description');
    var data = description.value;
    stompClient.send('/editDeck', {}, JSON.stringify(deck));
}
document.addEventListener('DOMContentLoaded', function() {
    var description = document.getElementById('description');
    description.addEventListener('input', function() {
        sendData();
    });
});