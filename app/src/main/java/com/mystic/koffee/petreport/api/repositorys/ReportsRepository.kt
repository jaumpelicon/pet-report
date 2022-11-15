package com.mystic.koffee.petreport.api.repositorys

import android.database.sqlite.SQLiteException
import com.mystic.koffee.petreport.api.dao.repositorys.ReportsDao
import com.mystic.koffee.petreport.api.model.ResponseState
import com.mystic.koffee.petreport.features.reportsScreen.models.ReportsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface ReportsRepositoryInterface {
    suspend fun insertReport(report: ReportsModel): Flow<ResponseState<Unit>>
    suspend fun removeReports(reportId: Long): Flow<ResponseState<Unit>>
    suspend fun getAllReports(): Flow<ResponseState<List<ReportsModel>>>
}

class ReportsRepository @Inject constructor(private val daoRepository: ReportsDao) :
    ReportsRepositoryInterface {

    override suspend fun insertReport(report: ReportsModel): Flow<ResponseState<Unit>> {
        return flow {
            try {
                daoRepository.insertReport(report)
                emit(ResponseState.Success(Unit))
            } catch (error: Exception) {
                emit(ResponseState.Error(Throwable("Error for insertReport")))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getAllReports(): Flow<ResponseState<List<ReportsModel>>> {
        return flow {
            try {
                val reportsList = daoRepository.getAllReports()
                emit(ResponseState.Success(reportsList))
            } catch (error: SQLiteException) {
                emit(ResponseState.Error(error))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun removeReports(reportId: Long): Flow<ResponseState<Unit>> {
        return flow{
            try {
                daoRepository.removeReport(reportId)
                daoRepository.cleanActionsByReport(reportId)

                emit(ResponseState.Success(Unit))
            }
            catch (error: Exception){
                emit(ResponseState.Error(error))
            }
        }.flowOn(Dispatchers.IO)
    }
}