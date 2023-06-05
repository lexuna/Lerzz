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

//Quiz erstellen
function sendInvitation(quiz) {
    var invitation = document.getElementById('invitation');
    client.send('app/invite', {}, invitation.value);
}

//var quizSocket = new SockJS('/lerzz');
//var quizClient = Stomp.over(quizSocket);
//quizClient.connect({}, function(frame) {
//        console.log('Connected: ' + frame);
//        quizClient.subscribe('/topic/question', function(message) {
//                console.log('Nachricht empfangen: ' + message.body);
//                var body = JSON.parse(message.body);
//                document.getElementById('quizQuestion').textContent = body.question;
//                document.getElementById('answer0').textContent = body.answers[0];
//                document.getElementById('answer1').textContent = body.answers[1];
//                document.getElementById('answer2').textContent = body.answers[2];
//                document.getElementById('answer3').textContent = body.answers[3];
//            });

//        quizClient.subscribe('/user/quiz/positions' + '-user' + quiz.ownerId, function(message) {
//                console.log('Nachricht empfangen: ' + message.body);
//
//                var pos0 = document.getElementById('qu'+message[0]+'pos0');
//                pos0.value=1
//                if(message.size() >= 2) {
//                    var pos1 = document.getElementById('qu'+message[1]+'pos1');
//                    pos1.value=2
//                }
//                if(message.size() >= 2) {
//                    var pos2 = document.getElementById('qu'+message[2]+'pos2');
//                    pos2.value=3
//                }
//                if(message.size() >= 2) {
//                    var pos3 = document.getElementById('qu'+message[3]+'pos3');
//                    pos3.value=4
//                }
//            });
//    });

//Quiz

//function sendCard(card, deckId) {
//
//    var headers = {
//      deckId: deckId
//    };
//    quizClient.send('/app/next', headers, JSON.stringify(card));
//}


