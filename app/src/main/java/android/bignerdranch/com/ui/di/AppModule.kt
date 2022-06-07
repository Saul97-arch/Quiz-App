package android.bignerdranch.com.ui.di

import android.bignerdranch.com.ui.cheat.CheatActivityViewModel
import org.koin.dsl.module

object AppModule {
    val viewModelModules = module {
        single { CheatActivityViewModel() }
    }
}