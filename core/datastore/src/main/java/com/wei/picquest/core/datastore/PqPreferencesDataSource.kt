package com.wei.picquest.core.datastore

import androidx.datastore.core.DataStore
import com.wei.picquest.core.model.data.DarkThemeConfig
import com.wei.picquest.core.model.data.LanguageConfig
import com.wei.picquest.core.model.data.ThemeBrand
import com.wei.picquest.core.model.data.UserData
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class PqPreferencesDataSource
    @Inject
    constructor(
        private val userPreferences: DataStore<UserPreferences>,
    ) {
        val userData =
            userPreferences.data.map {
                UserData(
                    isFirstTimeUser = it.isFirstTimeUser,
                    tokenString = it.tokenString,
                    userName = it.userName,
                    useDynamicColor = it.useDynamicColor,
                    themeBrand =
                        when (it.themeBrand) {
                            null,
                            ThemeBrandProto.THEME_BRAND_UNSPECIFIED,
                            ThemeBrandProto.UNRECOGNIZED,
                            ThemeBrandProto.THEME_BRAND_DEFAULT,
                            -> ThemeBrand.DEFAULT

                            ThemeBrandProto.THEME_BRAND_ANDROID -> ThemeBrand.ANDROID
                        },
                    darkThemeConfig =
                        when (it.darkThemeConfig) {
                            null,
                            DarkThemeConfigProto.DARK_THEME_CONFIG_UNSPECIFIED,
                            DarkThemeConfigProto.UNRECOGNIZED,
                            DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM,
                            -> DarkThemeConfig.FOLLOW_SYSTEM

                            DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT -> DarkThemeConfig.LIGHT
                            DarkThemeConfigProto.DARK_THEME_CONFIG_DARK -> DarkThemeConfig.DARK
                        },
                    languageConfig =
                        when (it.languageConfig) {
                            null,
                            LanguageConfigProto.LANGUAGE_CONFIG_UNSPECIFIED,
                            LanguageConfigProto.UNRECOGNIZED,
                            LanguageConfigProto.LANGUAGE_CONFIG_FOLLOW_SYSTEM,
                            -> LanguageConfig.FOLLOW_SYSTEM

                            LanguageConfigProto.LANGUAGE_CONFIG_ENGLISH -> LanguageConfig.ENGLISH
                            LanguageConfigProto.LANGUAGE_CONFIG_CHINESE -> LanguageConfig.CHINESE
                        },
                    recentSearchPhotoQueries = it.recentSearchPhotoQueriesList,
                    recentSearchVideoQueries = it.recentSearchVideoQueriesList,
                )
            }

        suspend fun setTokenString(tokenString: String) {
            try {
                userPreferences.updateData {
                    it.copy {
                        this.tokenString = tokenString
                    }
                }
            } catch (ioException: IOException) {
                Timber.tag("PqPreferences").e(ioException, "Failed to update user preferences")
            }
        }

        suspend fun addRecentSearchPhotoQuery(newQuery: String) {
            try {
                userPreferences.updateData { preferences ->
                    val updatedQueries =
                        preferences.recentSearchPhotoQueriesList.toMutableList().apply {
                            // 移除既有相同的查詢（如果存在），然後添加到列表末尾
                            remove(newQuery)
                            add(newQuery)
                            if (size > 10) removeAt(0)
                        }
                    preferences.toBuilder()
                        .clearRecentSearchPhotoQueries()
                        .addAllRecentSearchPhotoQueries(updatedQueries)
                        .build()
                }
            } catch (ioException: IOException) {
                Timber.tag("PqPreferencesDataSource")
                    .e(ioException, "Failed to update recent search photo queries")
            }
        }

        suspend fun clearRecentSearchPhotoQueries() {
            try {
                userPreferences.updateData { preferences ->
                    preferences.toBuilder()
                        .clearRecentSearchPhotoQueries()
                        .build()
                }
            } catch (ioException: IOException) {
                Timber.tag("PqPreferencesDataSource")
                    .e(ioException, "Failed to clear recent search photo queries")
            }
        }

        suspend fun addRecentSearchVideoQuery(newQuery: String) {
            try {
                userPreferences.updateData { preferences ->
                    val updatedQueries =
                        preferences.recentSearchVideoQueriesList.toMutableList().apply {
                            // 移除既有相同的查詢（如果存在），然後添加到列表末尾
                            remove(newQuery)
                            add(newQuery)
                            if (size > 10) removeAt(0)
                        }
                    preferences.toBuilder()
                        .clearRecentSearchVideoQueries()
                        .addAllRecentSearchVideoQueries(updatedQueries)
                        .build()
                }
            } catch (ioException: IOException) {
                Timber.tag("PqPreferencesDataSource")
                    .e(ioException, "Failed to update recent search video queries")
            }
        }

        suspend fun clearRecentSearchVideoQueries() {
            try {
                userPreferences.updateData { preferences ->
                    preferences.toBuilder()
                        .clearRecentSearchVideoQueries()
                        .build()
                }
            } catch (ioException: IOException) {
                Timber.tag("PqPreferencesDataSource")
                    .e(ioException, "Failed to clear recent search video queries")
            }
        }
    }
