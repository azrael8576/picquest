package com.wei.picquest.core.network.di

import com.wei.picquest.core.network.PqNetworkDataSource
import com.wei.picquest.core.network.retrofit.RetrofitPqNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * TODO Wei 移動至 build variants 下產出資料夾
 */
@Module
@InstallIn(SingletonComponent::class)
interface FlavoredNetworkModule {

    @Binds
    fun binds(implementation: RetrofitPqNetwork): PqNetworkDataSource
}
