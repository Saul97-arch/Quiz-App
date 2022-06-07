package android.bignerdranch.com.ui.cheat

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CheatActivityViewModel : ViewModel() {

    private val _answerIsTrue = MutableLiveData<Boolean>()
    val answerIsTrue: LiveData<Boolean> get() = _answerIsTrue


    fun onCheatButtonClicked() {

    }
}