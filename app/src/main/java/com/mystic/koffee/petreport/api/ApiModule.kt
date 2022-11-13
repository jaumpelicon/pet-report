package com.mystic.koffee.petreport.api

import com.mystic.koffee.petreport.api.dao.ReportsDao
import com.mystic.koffee.petreport.api.repositorys.ReportsRepository
import com.mystic.koffee.petreport.api.repositorys.ReportsRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ApiModule {


    @Provides
    fun provideReportsRepository(reportsDao: ReportsDao): ReportsRepositoryInterface =
        ReportsRepository(reportsDao)
}