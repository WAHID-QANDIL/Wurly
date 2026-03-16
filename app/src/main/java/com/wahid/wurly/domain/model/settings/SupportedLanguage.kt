package com.wahid.wurly.domain.model.settings


enum class SupportedLanguage(
    val code: String,
    val displayName: String,
) {
    ENGLISH(code = "EN", displayName = "English"),
    ARABIC(code = "AR", displayName = "Arabic")
}