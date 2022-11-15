package com.mystic.koffee.petreport.api

import com.mystic.koffee.petreport.api.dao.repositorys.ActionsDao
import com.mystic.koffee.petreport.api.dao.repositorys.ReportsDao
import com.mystic.koffee.petreport.api.repositorys.ActionsRepository
import com.mystic.koffee.petreport.api.repositorys.ActionsRepositoryInterface
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

    @Provides
    fun provideActionsRepository(actionsDao: ActionsDao): ActionsRepositoryInterface =
        ActionsRepository(actionsDao)
}