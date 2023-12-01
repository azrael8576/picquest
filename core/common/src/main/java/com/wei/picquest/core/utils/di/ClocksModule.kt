package com.wei.picquest.core.utils.di

import com.wei.picquest.core.utils.Clocks
import com.wei.picquest.core.utils.PqClocks
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import java.time.ZoneOffset

@Module
@InstallIn(SingletonComponent::class)
class ClocksModule {
    /**
     * 提供 Clock.systemDefaultZone() 的實例，可以在需要進行 DefaultZone 時間操作中使用。
     *
     * @return Clock.systemDefaultZone() 的實例
     */
    @Provides
    @Clocks(PqClocks.DefaultClock)
    fun provideSystemDefaultZoneClock(): Clock = Clock.systemDefaultZone()

    /**
     * 提供 Clock.system(ZoneOffset.UTC) 的實例，可以在需要進行 UTC 時間操作中使用。
     *
     * @return Clock.system(ZoneOffset.UTC) 的實例
     */
    @Provides
    @Clocks(PqClocks.UtcClock)
    fun provideUTCClock(): Clock = Clock.system(ZoneOffset.UTC)
}
