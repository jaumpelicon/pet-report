package com.mystic.koffee.petreport.features.actions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mystic.koffee.petreport.api.model.ResponseState
import com.mystic.koffee.petreport.api.repositorys.ActionsRepositoryInterface
import com.mystic.koffee.petreport.features.actions.addActionScreen.models.ActionsModel
import com.mystic.koffee.petreport.models.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActionsViewModel @Inject constructor(private val actionsRepository: ActionsRepositoryInterface) :
    ViewModel() {

    val insertActionsState get() = _insertActionsState.asStateFlow()
    private var _insertActionsState = MutableStateFlow<ViewState<Unit>?>(null)
    val getActionsState get() = _getActionsState.asStateFlow()
    private var _getActionsState = MutableStateFlow<ViewState<List<ActionsModel>>?>(null)
    val deleteActionState get() = _deleteActionState.asStateFlow()
    private var _deleteActionState = MutableStateFlow<ViewState<Unit>?>(null)

    fun insertAction(action: ActionsModel) {
        viewModelScope.launch {
            actionsRepository.insertAction(action).onStart {
                _insertActionsState.value = ViewState.Loading
            }.collect { state ->
                when (state) {
                    is ResponseState.Success -> {
                        _insertActionsState.value = ViewState.Success(Unit)
                    }
                    is ResponseState.Error -> {
                        _insertActionsState.value = ViewState.Error(Throwable("Response Error"))
                    }
                }
            }
        }
    }

    fun getActions(reportId: Long) {
        viewModelScope.launch {
            actionsRepository.getAllActions(reportId).onStart {
                _getActionsState.value = ViewState.Loading
            }.collect { state ->
                when (state) {
                    is ResponseState.Success -> {
                        _getActionsState.value = ViewState.Success(state.value)
                    }
                    is ResponseState.Error -> {
                        _getActionsState.value = ViewState.Error(Throwable("Response Error"))
                    }
                }
            }
        }
    }

    fun deleteAction(actionId: Long) {
        viewModelScope.launch {
            actionsRepository.removeActions(actionId).onStart {
                _deleteActionState.value = ViewState.Loading
            }.collect { state ->
                when (state) {
                    is ResponseState.Success -> {
                        _deleteActionState.value = ViewState.Success(state.value)
                    }
                    is ResponseState.Error -> {
                        _deleteActionState.value = ViewState.Error(Throwable("Response Error"))
                    }
                }
            }
        }
    }
}