package com.wei.picquest.core.data.test

import com.wei.picquest.core.data.di.DataModule
import com.wei.picquest.core.data.repository.DefaultSearchImagesRepository
import com.wei.picquest.core.data.repository.DefaultSearchVideosRepository
import com.wei.picquest.core.data.repository.DefaultUserDataRepository
import com.wei.picquest.core.data.repository.SearchImagesRepository
import com.wei.picquest.core.data.repository.SearchVideosRepository
import com.wei.picquest.core.data.repository.UserDataRepository
import com.wei.picquest.core.data.utils.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class],
)
interface TestDataModule {
    @Binds
    fun bindsSearchImagesRepository(searchImagesRepository: DefaultSearchImagesRepository): SearchImagesRepository

    @Binds
    fun bindsSearchVideosRepository(searchVideosRepository: DefaultSearchVideosRepository): SearchVideosRepository

    @Binds
    fun bindsNetworkMonitor(networkMonitor: AlwaysOnlineNetworkMonitor): NetworkMonitor

    @Binds
    fun bindsUserDataRepository(userDataRepository: DefaultUserDataRepository): UserDataRepository
}
