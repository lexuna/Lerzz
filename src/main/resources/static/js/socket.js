//Deck Bearbeitung
var deckSocket = new SockJS('/editDeck');
var deckStompClient = Stomp.over(deckSocket);
deckStompClient.connect({}, function(frame) {
    console.log('Verbunden: ' + frame);
});
function sendData(deck) {
    var description = document.getElementById('description');
    var name = document.getElementById('name');
    var data = description.value;
    deck.description = description.value;
    deckStompClient.send('/editDeck', {}, JSON.stringify(deck));
}

//Quiz erstellen
var inviteSocket = new SockJS('/invite');
var inviteStompClient = Stomp.over(inviteSocket);
inviteStompClient.connect({}, function(frame) {
    console.log('Verbunden: ' + frame);
});
function sendInvitation(quiz) {
    var invitation = document.getElementById('invitation');
    inviteStompClient.send('/invite', {}, invitation.value);
}

stompClient.subscribe('/topic/notification', function(message) {
        console.log('Nachricht empfangen: ' + message.body);
        // Hier kannst du den empfangenen Inhalt verarbeiten
    });


