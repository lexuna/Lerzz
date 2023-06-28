
var quizOwner;
var quizId;

function hideInvitationBanner() {
    let invitationBanner = document.getElementById("invitation-banner");

    if (invitationBanner) {
        invitationBanner.classList.add("hidden");
    } else {
        console.log("Einladungsbanner wurde nicht gefunden");
    }
}
function showInvitationBanner(inviteUser, quizName) {
    let invitationBanner = document.getElementById("invitation-banner");
    let invitationText = document.getElementById("invitation-text");

    if (invitationBanner && invitationText) {
        invitationText.innerHTML = inviteUser + " lädt dich zu " + quizName + " ein.";
        if (invitationBanner.classList.contains('hidden')) {
            invitationBanner.classList.remove("hidden");
        } else {
            console.log("Das Einladungsbanner wird bereits angezeigt");
        }
    } else {
        console.log("Ein oder mehrere Elemente wurden nicht gefunden");
    }
}

window.onload = function() {
    document.getElementById("invitation-banner").classList.add("hidden");

    let acceptButton = document.getElementById("accept-button");
    let declineButton = document.getElementById("decline-button");

    if (acceptButton && declineButton) {
        acceptButton.addEventListener("click", function() {
            hideInvitationBanner();
            var xhttp = new XMLHttpRequest();
            xhttp.open("POST", "/invitationAccepted", true);
            xhttp.setRequestHeader('Content-Type', 'application/json');
            xhttp.send(quizOwner);

            const currentUrl = window.location.host;
            const newUrl =  "/join/" + quizId;
            window.location.href = newUrl;
        });

        declineButton.addEventListener("click", function() {
            hideInvitationBanner();
            quizOwner='';
            quizLink='';
        });
    } else {
        console.log("Ein oder mehrere Elemente wurden nicht gefunden");
    }
};

function simulateInvitation() {
    // Setzen Sie hier die Werte für inviteUser und quizName
    let inviteUser = "Beispiel-Benutzer";
    let quizName = "Beispiel-Quiz";

    // Simulieren Sie eine Einladung, indem Sie showInvitationBanner() aufrufen
    showInvitationBanner(inviteUser, quizName);
}

//Socket
var socket = new SockJS('/lerzz/invite');
var client = Stomp.over(socket);
var sessionId = "";

client.connect({}, function(frame) {
    var url = client.ws._transport.url;
    url = url.replace("ws://localhost:8080/lerzz/invite",  "");
    url = url.replace("/websocket", "");
    url = url.replace(/^[0-9]+\//, "");
    console.log("Your current session is: " + url);
    sessionId = url;

    client.subscribe('/user/queue/quiz/invite', function(response) {
            var message = JSON.parse(response.body);
            // Verarbeite die empfangene Nachricht
            console.log(message);
            quizOwner = message.owner;
            quizId = message.quizId;
            showInvitationBanner(quizOwner, message.quiz);
    });
});
