package com.mystic.koffee.petreport.api.repositorys

import android.database.sqlite.SQLiteException
import com.mystic.koffee.petreport.api.dao.repositorys.ActionsDao
import com.mystic.koffee.petreport.api.model.ResponseState
import com.mystic.koffee.petreport.features.actions.addActionScreen.models.ActionsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface ActionsRepositoryInterface {
    suspend fun insertAction(action: ActionsModel): Flow<ResponseState<Unit>>
    suspend fun removeActions(actionId: Long): Flow<ResponseState<Unit>>
    suspend fun getAllActions(reportId: Long): Flow<ResponseState<List<ActionsModel>>>
}

class ActionsRepository @Inject constructor(private val daoRepository: ActionsDao) :
    ActionsRepositoryInterface {

    override suspend fun insertAction(action: ActionsModel): Flow<ResponseState<Unit>> {
        return flow {
            try {
                daoRepository.insertAction(action)
                emit(ResponseState.Success(Unit))
            } catch (error: Exception) {
                emit(ResponseState.Error(Throwable("Error for insertAction")))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun removeActions(actionId: Long): Flow<ResponseState<Unit>> {
        return flow {
            try {
                daoRepository.removeAction(actionId)
                emit(ResponseState.Success(Unit))
            } catch (error: Exception) {
                emit(ResponseState.Error(error))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getAllActions(reportId: Long): Flow<ResponseState<List<ActionsModel>>> {
        return flow {
            try {
                val actionsList = daoRepository.getAllActions(reportId)
                emit(ResponseState.Success(actionsList))
            } catch (error: SQLiteException) {
                emit(ResponseState.Error(error))
            }
        }.flowOn(Dispatchers.IO)
    }
}