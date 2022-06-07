package android.bignerdranch.com.ui.cheat

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.bignerdranch.com.R
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class CheatActivity : AppCompatActivity() {

    //private val viewModel : CheatActivityViewModel by viewModel

    private var answerIsTrue: Boolean? = null
    private var answerTextView: TextView? = null
    private var showAnswerButton: Button? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupListeners() {
        showAnswerButton?.setOnClickListener {
            setTextAnswer(answerIsTrue as Boolean)
            animateButton()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun animateButton() {
        val cx: Int? = showAnswerButton?.width?.div(2)
        val cy: Int? = showAnswerButton?.height?.div(2)
        val radius: Int? = showAnswerButton?.width

        if (cx != null && cy != null && radius != null) {
            val anim = ViewAnimationUtils
                .createCircularReveal(
                    showAnswerButton,
                    cx,
                    cy,
                    radius.toFloat(),
                    0.0F
                )

            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    showAnswerButton?.visibility = View.INVISIBLE
                }
            })
            anim.start()
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