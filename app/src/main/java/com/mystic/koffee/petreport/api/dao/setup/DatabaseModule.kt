package com.mystic.koffee.petreport.api.dao.setup

import android.content.Context
import com.mystic.koffee.petreport.api.dao.ReportsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideReportsDao(@ApplicationContext appContext: Context): ReportsDao {
        return PetReportRoom.getInstance(appContext).reportDao
    }
}