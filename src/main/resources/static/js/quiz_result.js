const correctAnswers = {
    question1: 'question1_answer1',
    question2: 'question2_answer3',
    question3: 'question3_answer3',
};

const answeredQuestions = {
    question1: 'question1_answer2',
    question2: 'question2_answer4',
    question3: 'question3_answer3',
};

function displayResults() {
    for (const [questionId, correctAnswerId] of Object.entries(correctAnswers)) {
        const correctElement = document.querySelector(`input[id="${correctAnswerId}"]`);
        const answeredAnswerId = answeredQuestions[questionId];
        const selectedElement = document.querySelector(`input[id="${answeredAnswerId}"]`);

        if (selectedElement) {
            selectedElement.checked = true;

            const selectedAnswerParent = selectedElement.closest('.answer');
            const correctAnswerParent = correctElement.closest('.answer');

            if (answeredAnswerId === correctAnswerId) {
                // Add a checkmark icon for correct answer
                selectedAnswerParent.innerHTML += `<i class="fas fa-check icon-overlay" style="color: #18853A;"></i>`;
                selectedAnswerParent.querySelector('label').style.color = '#18853A'; // Change the text color to green
            } else {
                // Add a cross icon for incorrect answer
                selectedAnswerParent.innerHTML += `<i class="fas fa-times icon-overlay" style="color: #E74C3C;"></i>`;
                selectedAnswerParent.querySelector('label').style.color = '#E74C3C'; // Change the text color to red
                // Add a checkmark icon for correct answer
                correctAnswerParent.innerHTML += `<i class="fas fa-check icon-overlay" style="color: #18853A;"></i>`;
                correctAnswerParent.querySelector('label').style.color = '#18853A'; // Change the text color to green
            }

        }
    }
}

function changeBackgroundColor(questionId, isCorrect) {
    const questionElement = document.getElementById(questionId);

    if (isCorrect) {
        questionElement.style.backgroundColor = '#18853A';
    } else {
        questionElement.style.backgroundColor = '#E74C3C';
    }
}

function updateQuestionBackground() {
    for (const [questionId, correctAnswerId] of Object.entries(correctAnswers)) {
        const answeredAnswerId = answeredQuestions[questionId];
        const isCorrect = answeredAnswerId === correctAnswerId;
        changeBackgroundColor(questionId, isCorrect);
    }
}

document.addEventListener('DOMContentLoaded', displayResults);
document.addEventListener('DOMContentLoaded', updateQuestionBackground);
