document.getElementById("details0").style.disabled = false;
document.getElementById("details0").addEventListener("click", loadDetails);
function loadDetails () {
    window.location.href = window.location.href+'/details';
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

  client.subscribe('/user/queue/quiz/newStats', function(response) {
      var message = JSON.parse(response.body);
      document.getElementById("details0").style.disabled = true;
      document.getElementById("user"+message.nr).textContent = message.user;
      document.getElementById("time"+message.nr).textContent = message.time;
      document.getElementById("answers"+message.nr).textContent = message.rightAnswer+"/10";
      document.getElementById("details"+message.nr).style.disabled = false;
      document.getElementById("details"+message.nr).addEventListener("click", loadDetails);
  });

});