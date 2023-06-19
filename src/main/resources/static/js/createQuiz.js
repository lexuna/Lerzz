function changeMode(mode) {
    var xhttp = new XMLHttpRequest();
    xhttp.open("POST", "/changeMode", true);
    xhttp.setRequestHeader('Content-Type', 'application/json');
    xhttp.send(mode);
}

function invite(name) {
    var xhttp = new XMLHttpRequest();
    xhttp.open("POST", "/invite", true);
    xhttp.setRequestHeader('Content-Type', 'application/json');
    xhttp.send(name);

    document.getElementById('invitation').value = '';
}

window.onload = function() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var message = JSON.parse(xhttp.responseText);
            
            for(var i=0; i<message.length; i++) {
                document.getElementById('invited'+i).textContent = message[i];
            }
            // document.getElementById('invited0').textContent = message[0];
            // if(message.length>=2) {
            //     document.getElementById('invited1').textContent = message[1];
            // }
            // if(message.length>=3) {
            //     document.getElementById('invited2').textContent = message[2];
            // }
            // if(message.length>=4) {
            //     document.getElementById('invited3').textContent = message[3];
            // }
        }
    };
    xhttp.open("POST", "/invitedPlayer", true);
    xhttp.send(window.location.href.includes('join'));

    if(window.location.href.includes('join')) {
        document.getElementById('start').disabled = true;
        document.getElementById('mode').disabled = true;
        document.getElementById('invite').disabled = true;
    }
}

//Socket
var socket = new SockJS('/lerzz');
var client = Stomp.over(socket);
var sessionId = "";

client.connect({}, function(frame) {
    var url = client.ws._transport.url;
    url = url.replace("ws://localhost:8080/lerzz",  "");
    url = url.replace("/websocket", "");
    url = url.replace(/^[0-9]+\//, "");
    console.log("Your current session is: " + url);
    sessionId = url;

    client.subscribe('/user/queue/quiz/invitationAccepted', function(response) {
        var message = response.body;
        // Verarbeite die empfangene Nachricht
        console.log(message);
        document.getElementById('invited1').textContent = message;
    });

    client.subscribe('/user/queue/quiz/start', function(response) {
        const currentUrl = window.location.host;
        const newUrl = currentUrl + response.body;
        window.location.href = response.body;
    });
});

var invitationButton = document.getElementById('invite');
var invitation = document.getElementById('invitation');
invitationButton.addEventListener('click', function (){
    invite(invitation.value);
});
var coopMode = document.getElementById('coopMode');
var vsMode = document.getElementById('vsMode');
coopMode.addEventListener('click', function (){
    changeMode('COOP');
});
vsMode.addEventListener('click', function (){
    changeMode('VS');
});
