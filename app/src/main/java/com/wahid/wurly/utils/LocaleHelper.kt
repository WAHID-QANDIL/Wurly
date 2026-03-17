package com.wahid.wurly.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {
    fun applyAppLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode.lowercase(Locale.ROOT))
        Locale.setDefault(locale)
        val resources = context.resources
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}