package android.bignerdranch.com.ui.quiz

import android.app.Activity
import android.bignerdranch.com.R
import android.bignerdranch.com.databinding.ActivityMainBinding
import android.bignerdranch.com.ui.cheat.CheatActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class QuizActivity : AppCompatActivity() {

    private val viewModel: QuizActivityViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.app_name)
        Log.d(TAG, "onCreateBundle called")
        subscribeEvents()
        setupListeners()
        updateQuestion()
        viewModel.resetCheatedValues()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            data?.let {
                viewModel.cheated = CheatActivity.wasAnswerShown(data)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.tvRemainingCheats?.text =
            getString(R.string.cheats_remaing, viewModel.cheatAttempts.value)
        disableCheatButton()
    }

    private fun subscribeEvents() {
        viewModel.question.observe(this) {
            binding.questionTextView.text = it
        }
        viewModel.isAnswerTrue.observe(this) {
            //viewModel.onCheckAnswer()
        }
    }

    private fun disableCheatButton() {
        if (viewModel.onDisableCheatButton()) {
            binding.cheatButton.isEnabled = false
        }
    }

    private fun newIntent() {
        val intent = Intent(this, CheatActivity::class.java).apply {
            putExtra(EXTRA_QUESTION_INDEX, viewModel.currentIndex)
        }

        startActivity(intent)
    }

    private fun setupListeners() {
        /*
        // this is the long implementation of setOnclickListener
        // note that in kotlin we use object instead of an anonymous inner class
        mTrueButton?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Does nothing yet, but soon!
            }
        })
        */
        binding.trueButton.setOnClickListener {
            checkAnswer(true)
            disableButtonForCurrentQuestion()
            displayFinalScore()
            if (viewModel.lastQuestion()) {
                resetScore()
            }
        }

        binding.falseButton.setOnClickListener {
            checkAnswer(false)
            disableButtonForCurrentQuestion()
            displayFinalScore()

            if (viewModel.lastQuestion()) {
                resetScore()
            }
        }

        binding.nextButton.setOnClickListener {
            goToNextQuestion()
            updateQuestion()
            enableButtonForTheNextQuestion()
        }

        binding.previousButton.setOnClickListener {
            goToPreviousQuestion()
            updateQuestion()
        }

        binding.cheatButton.setOnClickListener {
            viewModel.onCheatButtonClicked()
            newIntent()
        }
    }


    private fun displayFinalScore() {
        if (viewModel.isEndOfTheList()) {
            Toast.makeText(
                this,
                "You got  ${viewModel.getPercentageOfCorrectQuestions()}% of the questions!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun disableButtonForCurrentQuestion() {
        binding.trueButton.isEnabled = false
        binding.falseButton.isEnabled = false
    }

    private fun enableButtonForTheNextQuestion() {
        binding.trueButton.isEnabled = true
        binding.falseButton.isEnabled = true
    }

    private fun goToPreviousQuestion() {
        viewModel.goToFirstPosIfGoingToPreviousQuestion()
    }

    private fun goToNextQuestion() {
        viewModel.goToFirstPosIfIsGoingToNextQuestion()
    }

    private fun updateQuestion() {
        viewModel.updateQuestionLiveData()
    }

    private fun checkAnswer(userPressedTrue: Boolean) {
        viewModel.onCheckAnswer()
        val messageResId = if (viewModel.isCheater.value == true) {
            R.string.judgment_toast
        } else {
            if (userPressedTrue == viewModel.isAnswerTrue.value) {
                R.string.correct_toast
            } else {
                R.string.incorrect_toast
            }
        }

        viewModel.isAnswerTrue.value?.let { updateScore(userPressedTrue, it) }

        messageResId.let {
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun updateScore(userPressedTrue: Boolean, answerIsTrue: Boolean) {
        if (userPressedTrue == answerIsTrue) viewModel.incrementScore()
    }

    private fun resetScore() {
        viewModel.onResetScore()
    }

    companion object {
        private const val EXTRA_QUESTION_INDEX = "EXTRA_QUESTION_INDEX"
        private const val TAG = "QuizActivity"
    }
}