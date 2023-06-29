
var socket = new SockJS('/lerzz');
var client = Stomp.over(socket);
client.connect({}, function(frame) {
    console.log('Connected: ' + frame);
});

//Deck Bearbeitung
function sendData(deck) {
    var description = document.getElementById('description');
    var name = document.getElementById('deckName');
    deck.description = description.value;
    deck.name = name.value;
    client.send('/app/editDeck', {}, JSON.stringify(deck));
}
