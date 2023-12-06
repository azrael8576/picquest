package com.wei.picquest.core.data.repository

import com.wei.picquest.core.model.data.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    /**
     * Stream of [UserData]
     */
    val userData: Flow<UserData>

    /**
     * Sets the user's currently token
     */
    suspend fun setTokenString(tokenString: String)

    /**
     * Adds a new search query to the user's recent search photo queries.
     */
    suspend fun addRecentSearchPhotoQuery(newQuery: String)

    /**
     * Adds a new search query to the user's recent search video queries.
     */
    suspend fun addRecentSearchVideoQuery(newQuery: String)

    /**
     * Clears all the user's recent search photo queries.
     */
    suspend fun clearRecentSearchPhotoQueries()

    /**
     * Clears all the user's recent search video queries.
     */
    suspend fun clearRecentSearchVideoQueries()
}
