package android.bignerdranch.com

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class CheatActivity : AppCompatActivity() {

    private var answerIsTrue: Boolean? = null
    private var answerTextView: TextView? = null
    private var showAnswerButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        setupElements()

        savedInstanceState?.let {
            answerIsTrue = savedInstanceState.getBoolean(KEY_ANSWER_IS_TRUE)
        }

        getAnswer() ?: run {
            answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        }

        setupListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        answerIsTrue?.let { outState.putBoolean(KEY_ANSWER_IS_TRUE, it) }
    }

    private fun getAnswer(): Unit? {
        return answerIsTrue?.let {
            setTextAnswer(it)
        }
    }

    private fun setupElements() {
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
    }

    private fun setupListeners() {
        showAnswerButton?.setOnClickListener {
            setTextAnswer(answerIsTrue as Boolean)
        }
    }

    private fun setTextAnswer(answerTrue: Boolean) {
        if (answerTrue) {
            answerTextView?.setText(R.string.true_button)
        } else {
            answerTextView?.setText(R.string.false_button)
        }
        setAnswerShownResult()
    }

    private fun setAnswerShownResult() {
        val data = Intent()
        data.putExtra(EXTRA_ANSWER_SHOWN, true)
        setResult(RESULT_OK, data)
    }


    companion object {
        private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"
        private const val KEY_ANSWER_IS_TRUE = "answerIsTrue"
        private const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"
        fun wasAnswerShown(result: Intent): Boolean {
            return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)
        }
    }
}