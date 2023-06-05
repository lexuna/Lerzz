const radioButtons = document.querySelectorAll('input[type="radio"]');
var nextButton = document.getElementById('next');
var cardId;

// Event Listener für Radio-Buttons
radioButtons.forEach(radio => {
  radio.addEventListener('change', checkRadioButtons);
});

// Funktion zum Überprüfen der Radio-Button-Auswahl
function checkRadioButtons() {
  const checkedRadio = document.querySelector('input[type="radio"]:checked');
  nextButton.disabled = !checkedRadio;
}

nextButton.addEventListener('click' , setButtonText)

function setButtonText() {
    const questionNr = document.getElementById('questionNr');
    const questionList = document.querySelectorAll('div[name="questionList"]');
}

function loadNext(deckId, quizId) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
         if (this.readyState == 4 && this.status == 200) {
             if(xhttp.responseText == '') {
                endQuiz();
             } else {
                 var body = JSON.parse(xhttp.responseText);
                 cardId = body.card.id;
                 document.getElementById('quizQuestion').textContent = body.card.question;
                 document.getElementById('answer0').textContent = body.card.answers[0];
                 document.getElementById('answer1').textContent = body.card.answers[1];
                 document.getElementById('answer2').textContent = body.card.answers[2];
                 document.getElementById('answer3').textContent = body.card.answers[3];

                 radioButtons.forEach(radio => {
                   radio.checked = false;
                 });
                 nextButton.disabled = true;

                 if(body.lastCard) {
                     nextButton.textContent = 'Beenden';
                 }
             }
         }
    }
  };

   var body = {
        'deckId' : deckId,
        'quizId' : quizId,
        'cardId' : cardId,
        'solution' : getSolution()
   };

    if(nextButton.textContent == 'Beenden') {
        xhttp.open("POST", "/end", true);
        xhttp.setRequestHeader('Content-Type', 'application/json');
        xhttp.send(JSON.stringify(body));
    } else {
        xhttp.open("POST", "/next", true);
        xhttp.setRequestHeader('Content-Type', 'application/json');
        xhttp.send(JSON.stringify(body));
    }
}

function endQuiz() {
  const currentUrl = window.location.href;
  const newUrl = currentUrl + '/stats';
  window.location.href = newUrl;
}

function getSolution() {
    var solution;
    if (document.getElementById('solution0').checked) {
        solution = 0;
    }
    if (document.getElementById('solution1').checked) {
        solution = 1;
    }
    if (document.getElementById('solution2').checked) {
        solution = 2;
    }
    if (document.getElementById('solution3').checked) {
        solution = 3;
    }
    return solution;
}

document.addEventListener("DOMContentLoaded", function () {
    if(document.documentElement.id == 'quiz') {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                    var body = JSON.parse(xhttp.responseText);
                    cardId = body.card.id;
                    document.getElementById('quizQuestion').textContent = body.card.question;
                    document.getElementById('answer0').textContent = body.card.answers[0];
                    document.getElementById('answer1').textContent = body.card.answers[1];
                    document.getElementById('answer2').textContent = body.card.answers[2];
                    document.getElementById('answer3').textContent = body.card.answers[3];
                }
        };
        xhttp.open("POST", "/card", true);
        xhttp.setRequestHeader('Content-Type', 'application/json');
        var body = {
           'path' : window.location.pathname
        };
        xhttp.send(JSON.stringify(body));
    }
});

