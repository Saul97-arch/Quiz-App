package android.bignerdranch.com.ui.di

import android.bignerdranch.com.ui.cheat.CheatActivityViewModel
import android.bignerdranch.com.ui.quiz.QuizActivityViewModel
import org.koin.dsl.module

object AppModule {
    val viewModelModules = module {
        single { CheatActivityViewModel() }
        single { QuizActivityViewModel() }
    }
}