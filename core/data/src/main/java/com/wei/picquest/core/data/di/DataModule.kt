package com.wei.picquest.core.data.di

import com.wei.picquest.core.data.repository.DefaultSearchImagesRepository
import com.wei.picquest.core.data.repository.DefaultSearchVideosRepository
import com.wei.picquest.core.data.repository.DefaultUserDataRepository
import com.wei.picquest.core.data.repository.SearchImagesRepository
import com.wei.picquest.core.data.repository.SearchVideosRepository
import com.wei.picquest.core.data.repository.UserDataRepository
import com.wei.picquest.core.data.utils.ConnectivityManagerNetworkMonitor
import com.wei.picquest.core.data.utils.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsSearchImagesRepository(
        searchImagesRepository: DefaultSearchImagesRepository,
    ): SearchImagesRepository

    @Binds
    internal abstract fun bindsSearchVideosRepository(
        searchVideosRepository: DefaultSearchVideosRepository,
    ): SearchVideosRepository

    @Binds
    internal abstract fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor

    @Binds
    internal abstract fun bindsUserDataRepository(
        userDataRepository: DefaultUserDataRepository,
    ): UserDataRepository
}
