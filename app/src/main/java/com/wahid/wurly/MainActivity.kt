package com.wahid.wurly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wahid.wurly.presentation.common.AppSettingsViewModel
import com.wahid.wurly.presentation.common.ProvideUserSettings
import com.wahid.wurly.presentation.framwork.MainActivityLifecycleObserver
import com.wahid.wurly.presentation.navigation.AppNavigation
import com.wahid.wurly.ui.theme.WurlyTheme
import com.wahid.wurly.utils.LocaleHelper
import com.wahid.wurly.work.WeatherAlertScheduler
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycle.addObserver(MainActivityLifecycleObserver.getInstance(this))
        super.onCreate(savedInstanceState)
        WeatherAlertScheduler.scheduleDailySync(this)
        enableEdgeToEdge()
        setContent {
            val appSettingsViewModel: AppSettingsViewModel = hiltViewModel()
            val userSettings by appSettingsViewModel.settings.collectAsStateWithLifecycle()

            LaunchedEffect(userSettings.language) {
                val targetLanguage = userSettings.language.lowercase(Locale.ROOT)
                val currentLanguage = resources.configuration.locales[0]?.language
                    ?: Locale.getDefault().language
                if (currentLanguage != targetLanguage) {
                    LocaleHelper.applyAppLocale(this@MainActivity, userSettings.language)
                    recreate()
                }
            }

            val layoutDirection = when (userSettings.language.uppercase(Locale.ROOT)) {
                "AR" -> LayoutDirection.Rtl
                else -> LayoutDirection.Ltr
            }

            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                ProvideUserSettings(userSettings) {
                    WurlyTheme {
                        AppNavigation()
                    }
                }
            }
        }
    }
}