package android.bignerdranch.com.ui.cheat

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.bignerdranch.com.R
import android.bignerdranch.com.databinding.ActivityCheatBinding
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class CheatActivity : AppCompatActivity() {

    private val viewModel: CheatActivityViewModel by viewModel()
    private lateinit var binding: ActivityCheatBinding
    private var answerIsTrue: Boolean? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

    private fun subscribeEvents() {
        // viewModel.answerIsTrue.observe(this, )
    }

    private fun getAnswer(): Unit? {
        return answerIsTrue?.let {
            setTextAnswer(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupListeners() {
        binding.showAnswerButton.setOnClickListener {
            setTextAnswer(answerIsTrue as Boolean)
            animateButton()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun animateButton() {
        val cx: Int = binding.showAnswerButton.width.div(2)
        val cy: Int = binding.showAnswerButton.height.div(2)
        val radius: Int = binding.showAnswerButton.width

        val anim = ViewAnimationUtils
            .createCircularReveal(
                binding.showAnswerButton,
                cx,
                cy,
                radius.toFloat(),
                0.0F
            )

        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                binding.showAnswerButton.visibility = View.INVISIBLE
            }
        })
        anim.start()

    }

    private fun setTextAnswer(answerTrue: Boolean) {
        if (answerTrue) {
            binding.answerTextView.setText(R.string.true_button)
        } else {
            binding.answerTextView.setText(R.string.false_button)
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