package com.wei.picquest.core.model.data

/**
 * Class summarizing user preferences data
 */
data class UserData(
    val isFirstTimeUser: Boolean,
    val tokenString: String,
    val userName: String,
    val useDynamicColor: Boolean,
    val themeBrand: ThemeBrand,
    val darkThemeConfig: DarkThemeConfig,
    val languageConfig: LanguageConfig,
    val recentSearchQueries: List<String>,
)
