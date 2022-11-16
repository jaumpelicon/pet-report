package com.mystic.koffee.petreport.features.reportsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mystic.koffee.petreport.api.model.ResponseState
import com.mystic.koffee.petreport.api.repositorys.ReportsRepositoryInterface
import com.mystic.koffee.petreport.features.reportsScreen.models.ReportsModel
import com.mystic.koffee.petreport.models.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReportsViewModel @Inject constructor(private val reportsRepository: ReportsRepositoryInterface) :
    ViewModel() {

    val insertReportsState get() = _insertReportsState.asStateFlow()
    private var _insertReportsState = MutableStateFlow<ViewState<Unit>?>(null)
    val getReportsState get() = _getReportsState.asStateFlow()
    private var _getReportsState = MutableStateFlow<ViewState<List<ReportsModel>>?>(null)
    val deleteReportState get() = _deleteReportState.asStateFlow()
    private var _deleteReportState = MutableStateFlow<ViewState<Unit>?>(null)

    init {
        getReports()
    }

    fun insertReport(report: ReportsModel) {
        viewModelScope.launch {
            reportsRepository.insertReport(report).onStart {
                _insertReportsState.value = ViewState.Loading
            }.collect { state ->
                when (state) {
                    is ResponseState.Success -> {
                        _insertReportsState.value = ViewState.Success(Unit)
                    }
                    is ResponseState.Error -> {
                        _insertReportsState.value = ViewState.Error(Throwable("Response Error"))
                    }
                }
            }
        }
    }

    fun getReports() {
        viewModelScope.launch {
            reportsRepository.getAllReports().onStart {
                _getReportsState.value = ViewState.Loading
            }.collect { state ->
                when (state) {
                    is ResponseState.Success -> {
                        _getReportsState.value = ViewState.Success(state.value)
                    }
                    is ResponseState.Error -> {
                        _getReportsState.value = ViewState.Error(Throwable("Response Error"))
                    }
                }
            }
        }
    }

    fun deleteReport(reportId: Long) {
        viewModelScope.launch {
            reportsRepository.removeReports(reportId).onStart {
                _deleteReportState.value = ViewState.Loading
            }.collect { state ->
                when (state) {
                    is ResponseState.Success -> {
                        _deleteReportState.value = ViewState.Success(state.value)
                    }
                    is ResponseState.Error -> {
                        _deleteReportState.value = ViewState.Error(Throwable("Response Error"))
                    }
                }
            }
        }
    }
}