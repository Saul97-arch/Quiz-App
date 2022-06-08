package android.bignerdranch.com.ui.cheat

import android.bignerdranch.data.model.Question
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CheatActivityViewModel : ViewModel() {

    private val questionBank = Question.questionBank
    private val _answerIsTrue = MutableLiveData<Boolean>()
    val answerIsTrue: LiveData<Boolean> get() = _answerIsTrue

    fun onCheatButtonClicked() {
        CurrentQuestionInfo.index?.let {
            _answerIsTrue.value = questionBank[it].isAnswerTrue
        }
    }
}