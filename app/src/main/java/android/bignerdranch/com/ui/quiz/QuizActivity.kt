package android.bignerdranch.com.ui.quiz

import android.app.Activity
import android.bignerdranch.com.R
import android.bignerdranch.com.ui.cheat.CheatActivity
import android.bignerdranch.data.model.Question
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class QuizActivity : AppCompatActivity() {

    private var trueButton: Button? = null
    private var falseButton: Button? = null
    private var nextButton: Button? = null
    private var previousButton: Button? = null
    private var mQuestionTextView: TextView? = null
    private var cheatsRemainingTextView: TextView? = null
    private var cheatButton: Button? = null
    private var currentIndex: Int = 0
    private var currentScore: Int = 0
    private var cheatAttempts: Int = 3

    private val questionBank = Question.questionBank

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle(R.string.app_name)
        Log.d(TAG, "onCreateBundle called")

        savedInstanceState?.let {
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0)
            questionBank[currentIndex].isCheaterOnQuestion =
                savedInstanceState.getBoolean(KEY_CHEATER, false)
        }

        setupElements()
        setupListeners()
        updateQuestion()
        resetCheatedValues()
        setCheatsRemainingText()
    }

    override fun onResume() {
        super.onResume()
        if (cheatAttempts == 0) {
            cheatButton?.isEnabled = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            data?.let {
                questionBank[currentIndex].isCheaterOnQuestion = CheatActivity.wasAnswerShown(data)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX, currentIndex)
        outState.putInt(KEY_CHEAT_ATTEMPTS, cheatAttempts)
        outState.putBoolean(KEY_CHEATER, questionBank[currentIndex].isCheaterOnQuestion)
    }

    private fun newIntent() {
        val intent = Intent(this, CheatActivity::class.java).apply {
            putExtra(EXTRA_ANSWER_IS_TRUE, questionBank[currentIndex].isAnswerTrue)
        }
        startActivityForResult(intent, REQUEST_CODE_CHEAT)
    }

    private fun resetCheatedValues() {
        if (currentIndex + 1 == questionBank.size) {
            questionBank.map { question ->
                question.isCheaterOnQuestion = false
            }
        }
    }

    private fun setupElements() {
        mQuestionTextView = findViewById(R.id.question_text_view)
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        previousButton = findViewById(R.id.previous_button)
        cheatButton = findViewById(R.id.cheat_button)
        cheatsRemainingTextView = findViewById(R.id.tv_remainingCheats)
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
        trueButton?.setOnClickListener {
            checkAnswer(true)
            disableButtonForCurrentQuestion()
            displayFinalScore()
            if (lastQuestion()) {
                resetScore()
            }
        }

        falseButton?.setOnClickListener {
            checkAnswer(false)
            disableButtonForCurrentQuestion()
            displayFinalScore()

            if (lastQuestion()) {
                resetScore()
            }
        }

        nextButton?.setOnClickListener {
            goToNextQuestion()
            updateQuestion()
            enableButtonForTheNextQuestion()
        }

        previousButton?.setOnClickListener {
            goToPreviousQuestion()
            updateQuestion()
        }

        cheatButton?.setOnClickListener {
            cheatAttempts--
            setCheatsRemainingText()
            newIntent()
        }
    }

    private fun setCheatsRemainingText() {
        cheatsRemainingTextView?.text = getString(R.string.cheats_remaing, cheatAttempts)
    }

    private fun lastQuestion() = currentIndex == questionBank.size - 1

    private fun displayFinalScore() {
        if (currentIndex + 1 == questionBank.size) {
            Toast.makeText(
                this,
                "You got  ${getPercentageOfCorrectQuestions()}% of the questions!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getPercentageOfCorrectQuestions() =
        (currentScore.toDouble().div(questionBank.size) * 100).roundToInt()

    private fun disableButtonForCurrentQuestion() {
        trueButton?.isEnabled = false
        falseButton?.isEnabled = false
    }

    private fun enableButtonForTheNextQuestion() {
        trueButton?.isEnabled = true
        falseButton?.isEnabled = true
    }

    private fun goToPreviousQuestion() {
        // Cool logic to go to the initial position of the array when we get to the last pos
        if (currentIndex > 0) currentIndex = (currentIndex - 1) % questionBank.size
    }

    private fun goToNextQuestion() {
        // Cool logic to go to the initial position of the array when we get to the last pos
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    private fun updateQuestion() {
        val question = questionBank[currentIndex].question
        mQuestionTextView?.text = question
    }

    private fun checkAnswer(userPressedTrue: Boolean) {
        val answerIsTrue: Boolean = questionBank[currentIndex].isAnswerTrue

        val messageResId = if (questionBank[currentIndex].isCheaterOnQuestion) {
            R.string.judgment_toast
        } else {
            if (userPressedTrue == answerIsTrue) {
                R.string.correct_toast
            } else {
                R.string.incorrect_toast
            }
        }

        updateScore(userPressedTrue, answerIsTrue)

        messageResId.let {
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun updateScore(userPressedTrue: Boolean, answerIsTrue: Boolean) {
        if (userPressedTrue == answerIsTrue) currentScore++
    }

    private fun resetScore() {
        currentScore = 0
    }

    companion object {
        private const val TAG = "QuizActivity"
        private const val KEY_INDEX = "index"
        private const val KEY_CHEATER = "cheater_key"
        private const val KEY_CHEAT_ATTEMPTS = "cheat attempts key"
        private const val REQUEST_CODE_CHEAT = 0
        private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"
    }
}