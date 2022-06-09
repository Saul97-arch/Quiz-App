package android.bignerdranch.com.ui.cheat

import android.bignerdranch.data.model.Question
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CheatActivityViewModel : ViewModel() {

    var index = 0
    private val questionBank = Question.questionBank
    private val _answerIsTrue = MutableLiveData<Boolean>()
    val answerIsTrue: LiveData<Boolean> get() = _answerIsTrue

    fun onCheatButtonClicked() {
        questionBank[index].isCheaterOnQuestion = true
        _answerIsTrue.value = questionBank[index].isAnswerTrue
    }
}