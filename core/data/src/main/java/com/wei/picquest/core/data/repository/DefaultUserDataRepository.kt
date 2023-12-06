package com.wei.picquest.core.data.repository

import com.wei.picquest.core.datastore.PqPreferencesDataSource
import com.wei.picquest.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultUserDataRepository @Inject constructor(
    private val paPreferencesDataSource: PqPreferencesDataSource,
) : UserDataRepository {

    override val userData: Flow<UserData> =
        paPreferencesDataSource.userData
    override suspend fun setTokenString(tokenString: String) {
        paPreferencesDataSource.setTokenString(tokenString)
    }

    override suspend fun addRecentSearchPhotoQuery(newQuery: String) {
        paPreferencesDataSource.addRecentSearchPhotoQuery(newQuery)
    }

    override suspend fun addRecentSearchVideoQuery(newQuery: String) {
        paPreferencesDataSource.addRecentSearchVideoQuery(newQuery)
    }

    override suspend fun clearRecentSearchPhotoQueries() {
        paPreferencesDataSource.clearRecentSearchPhotoQueries()
    }

    override suspend fun clearRecentSearchVideoQueries() {
        paPreferencesDataSource.clearRecentSearchVideoQueries()
    }
}
