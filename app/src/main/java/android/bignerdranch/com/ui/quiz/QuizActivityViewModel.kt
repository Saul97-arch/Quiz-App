package android.bignerdranch.com.ui.quiz

import android.bignerdranch.data.model.Question
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt

class QuizActivityViewModel : ViewModel() {

    var currentIndex: Int = 0
    var cheated : Boolean? = null
    private val questionBank = Question.questionBank
    private var currentScore: Int = 0


    val cheatAttempts: LiveData<Int> get() = _cheatAttempts
    private val _cheatAttempts = MutableLiveData(3)

    private val _question = MutableLiveData<String>()
    val question : LiveData<String> get() = _question

    private val _isAnswerTrue = MutableLiveData<Boolean>()
    val isAnswerTrue : LiveData<Boolean> get() = _isAnswerTrue

    private val _isCheater = MutableLiveData<Boolean>()
    val isCheater : LiveData<Boolean> get() = _isCheater

    fun onDisableCheatButton(): Boolean {
        return cheatAttempts.value == 0
    }

    fun onCheatButtonClicked() {
        _cheatAttempts.postValue(
            _cheatAttempts.value?.minus(1)
        )
    }

    fun goToFirstPosIfGoingToPreviousQuestion() {
        // Cool logic to go to the initial position of the array when we get to the last pos
        if (currentIndex > 0) {
            currentIndex = (currentIndex - 1) % questionBank.size
        }
    }

    fun resetCheatedValues() {
        if (isEndOfTheList()) {
            questionBank.map { question ->
                question.isCheaterOnQuestion = false
            }
        }
    }

    fun lastQuestion() = currentIndex == questionBank.size - 1

    fun isEndOfTheList(): Boolean {
        return currentIndex + 1 == questionBank.size
    }

    // liveData
    fun getPercentageOfCorrectQuestions() =
        (currentScore.toDouble().div(questionBank.size).times(100).roundToInt())

    fun goToFirstPosIfIsGoingToNextQuestion() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun updateQuestionLiveData () {
        val question = questionBank[currentIndex]
        _question.postValue(question.question)
    }

    fun incrementScore() {
        currentScore++
    }

    fun onResetScore() {
        currentScore = 0
    }

    fun onCheckAnswer() {
        // TODO, descobrir quando usar postValue e atribuição
        _isAnswerTrue.value = questionBank[currentIndex].isAnswerTrue
        _isCheater.value = questionBank[currentIndex].isCheaterOnQuestion
    }
}