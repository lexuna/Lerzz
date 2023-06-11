//Socket
//var socket = new SockJS('/lerzz');
//var client = Stomp.over(socket);
//var sessionId = "";
//
//client.connect({}, function(frame) {
//    var url = client.ws._transport.url;
//    url = url.replace("ws://localhost:8080/lerzz",  "");
//    url = url.replace("/websocket", "");
//    url = url.replace(/^[0-9]+\//, "");
//    console.log("Your current session is: " + url);
//    sessionId = url;
//});

//Deck Bearbeitung
function sendData(deck) {
    var description = document.getElementById('description');
    var name = document.getElementById('deckName');
    deck.description = description.value;
    deck.name = name.value;
    client.send('app/editDeck', {}, JSON.stringify(deck));
}

////Quiz erstellen
//function sendInvitation(quiz) {
//    var invitation = document.getElementById('invitation');
//    client.send('app/invite', {}, invitation.value);
//}



//Quiz

//function sendCard(card, deckId) {
//
//    var headers = {
//      deckId: deckId
//    };
//    quizClient.send('/app/next', headers, JSON.stringify(card));
//}


