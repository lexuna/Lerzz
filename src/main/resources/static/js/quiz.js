const radioButtons = document.querySelectorAll('input[type="radio"]');
var nextButton = document.getElementById("next");
var cardId;
var quizId;

// Funktion zum Überprüfen der Radio-Button-Auswahl
function checkRadioButton() {
  const checkedRadio = document.querySelector('input[type="radio"]:checked');
  nextButton.disabled = !checkedRadio;
}

function clickRadioButton(radio) {
  var xhttp = new XMLHttpRequest();

  xhttp.open("POST", "/chose", true);
  xhttp.setRequestHeader("Content-Type", "application/json");
  var body = {
    radioId: radio.id,
    quizId: quizId,
  };
  xhttp.send(JSON.stringify(body));
}

//function setButtonText() {
//    const questionNr = document.getElementById('questionNr');
//    const questionList = document.querySelectorAll('div[name="questionList"]');
//}

function loadNext() {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function () {
    if (this.readyState == 4 && this.status == 200) {
      if (xhttp.responseText == "") {
        endQuiz();
      } else {
        nextCard(JSON.parse(xhttp.responseText));
      }
    }
  };

  var body = {
    quizId: quizId,
    cardId: cardId,
    solution: getSolution(),
  };

  if (nextButton.textContent == "Beenden") {
    xhttp.open("POST", "/end", true);
    xhttp.setRequestHeader("Content-Type", "application/json");
    xhttp.send(JSON.stringify(body));
  } else {
    xhttp.open("POST", "/next", true);
    xhttp.setRequestHeader("Content-Type", "application/json");
    xhttp.send(JSON.stringify(body));
  }
}

function nextCard(body) {
  updateCard(body.card);

  radioButtons.forEach((radio) => {
    radio.checked = false;
  });
  nextButton.disabled = true;

  if (body.lastCard) {
    nextButton.textContent = "Beenden";
  }
}

function endQuiz() {
  const currentUrl = window.location.href;
  const newUrl = currentUrl.replace('?', '') + "/stats";
  window.location.href = newUrl;
}

function getSolution() {
  var solution;
  if (document.getElementById("solution0").checked) {
    solution = 0;
  }
  if (document.getElementById("solution1").checked) {
    solution = 1;
  }
  if (document.getElementById("solution2").checked) {
    solution = 2;
  }
  if (document.getElementById("solution3").checked) {
    solution = 3;
  }
  return solution;
}

function updatePositions(positions) {
  for (var i = 0; i < positions.length; i++) {
    var newPos = document.getElementById("qu" + positions[i] + "pos" + i);
    newPos.textContent = i + 1;
    var oldPos = document.getElementById("qu" + (positions[i] - 1) + "pos" + i);
    if (oldPos != null) {
      oldPos.textContent = "";
    }
  }
}

function updateCard(card) {
  cardId = card.id;
  document.getElementById("quizQuestion").textContent = card.question;
  document.getElementById("answer0").textContent = card.answers[0];
  document.getElementById("answer1").textContent = card.answers[1];
  document.getElementById("answer2").textContent = card.answers[2];
  document.getElementById("answer3").textContent = card.answers[3];
}

//Socket
var socket = new SockJS("/lerzz");
var client = Stomp.over(socket);
var sessionId = "";

client.connect({}, function (frame) {
  var url = client.ws._transport.url;
  url = url.replace("ws://localhost:8080/lerzz", "");
  url = url.replace("/websocket", "");
  url = url.replace(/^[0-9]+\//, "");
  console.log("Your current session is: " + url);
  sessionId = url;

  client.subscribe("/user/queue/quiz/next", function (response) {
    nextCard(JSON.parse(response.body));
  });

  client.subscribe("/user/queue/quiz/chose", function (response) {
    var body = JSON.parse(response.body);
    document.getElementById(body).checked = true;
    nextButton.disabled = false;
  });

  client.subscribe("/user/queue/quiz/positions", function (response) {
    var body = JSON.parse(response.body);
    updatePositions(body);
  });
  client.subscribe("/user/queue/quiz/end", function (response) {
    endQuiz();
  });
});

//---------------------------------------------------------------------------------------

radioButtons.forEach((radio) => {
  radio.addEventListener("change", checkRadioButton);
  radio.addEventListener("click", function () {
    clickRadioButton(radio);
  });
});

//nextButton.addEventListener('click' , setButtonText)

nextButton.addEventListener("click", loadNext);

//---------------------------------------------------------------------------------------

document.addEventListener("DOMContentLoaded", function () {
  if (document.documentElement.id == "quiz") {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
      if (this.readyState == 4 && this.status == 200) {
        var body = JSON.parse(xhttp.responseText);
        quizId = body.quizId;
        cardId = body.card.id;
        updateCard(body.card);
      }
    };
    xhttp.open("POST", "/card", true);
    xhttp.setRequestHeader("Content-Type", "application/json");
    var body = {
      path: window.location.pathname,
    };
    xhttp.send(JSON.stringify(body));
  }
});
