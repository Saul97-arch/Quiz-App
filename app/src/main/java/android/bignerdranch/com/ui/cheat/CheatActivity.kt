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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class CheatActivity : AppCompatActivity() {

    private val viewModel: CheatActivityViewModel by viewModel()
    private lateinit var binding: ActivityCheatBinding

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribeEvents()
        viewModel.index = intent.getIntExtra(EXTRA_QUESTION_INDEX, 0)
        setupListeners()
    }

    private fun subscribeEvents() {
        viewModel.answerIsTrue.observe(this) {
            onCheat()
        }
    }

    private fun setAnswerShownResult() {
        val data = Intent()
        data.putExtra(EXTRA_ANSWER_SHOWN, true)
        setResult(RESULT_OK, data)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupListeners() {
        binding.showAnswerButton.setOnClickListener {
            viewModel.onCheatButtonClicked()
            setAnswerShownResult()
            setTextAnswer()
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

    private fun setTextAnswer() {
        if (viewModel.answerIsTrue.value == true) {
            binding.answerTextView.setText(R.string.true_button)
        } else {
            binding.answerTextView.setText(R.string.false_button)
        }
    }

    private fun onCheat() {
        Toast.makeText(this, "You cheated and I saw it!", Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val EXTRA_QUESTION_INDEX = "EXTRA_QUESTION_INDEX"
        private const val EXTRA_ANSWER_SHOWN = "EXTRA_ANSWER_SHOWN"
        fun wasAnswerShown(result : Intent) : Boolean{
            return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)
        }
    }
}