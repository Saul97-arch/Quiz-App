package android.bignerdranch.com.ui

import android.app.Application
import android.bignerdranch.com.ui.di.AppModule
import org.koin.core.context.startKoin

class GeoQuizApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(AppModule.viewModelModules)
        }
    }
}