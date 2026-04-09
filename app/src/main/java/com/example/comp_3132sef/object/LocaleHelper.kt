package com.example.comp_3132sef.`object`

import android.content.Context
import android.content.ContextWrapper
import java.util.Locale

object LocaleHelper {
    fun wrap(context: Context, languageTag: String): ContextWrapper {
        val locale = Locale.forLanguageTag(languageTag)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)


        val newContext = context.createConfigurationContext(config)
        return ContextWrapper(newContext)
    }
}