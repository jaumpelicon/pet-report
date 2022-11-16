package com.mystic.koffee.petreport.features.share

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.mystic.koffee.petreport.api.model.ResponseState
import com.mystic.koffee.petreport.api.repositorys.ActionsRepositoryInterface
import com.mystic.koffee.petreport.features.actions.addActionScreen.models.ActionsModel
import com.mystic.koffee.petreport.models.ViewState
import com.mystic.koffee.petreport.support.Constants.PETREPORT_SHARED_FILE_NAME
import com.mystic.koffee.petreport.support.extension.generateFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(private val actionsRepository: ActionsRepositoryInterface) :
    ViewModel() {

    val shareState get() = _shareState.asStateFlow()
    private val _shareState = MutableStateFlow<ViewState<URI>?>(null)

    fun shareData(context: Context, reportId: Long) {
        getData(context, reportId)
    }

    private fun getData(context: Context, reportId: Long) {
        _shareState.value = ViewState.Loading
        viewModelScope.launch {
            actionsRepository.getAllActions(reportId).collect {
                when (it) {
                    is ResponseState.Success<*> -> {
                        exportWeightsToCsv(it.value as List<ActionsModel>, context)
                    }
                    is ResponseState.Error -> {
                        _shareState.value = ViewState.Error(Throwable("Error in database"))
                    }
                }
            }
        }
    }

    private fun exportWeightsToCsv(actionList: List<ActionsModel>, context: Context) {
        val csvFile = generateCsvFile(context)
        if (csvFile != null) {
            writeDataInsideCsv(context, csvFile, actionList)
            _shareState.value = ViewState.Success(csvFile.toURI())
        } else {
            _shareState.value = ViewState.Error(Throwable("Error export CSV"))
        }
    }

    private fun generateCsvFile(context: Context): File? {
        return generateFile(context, PETREPORT_SHARED_FILE_NAME)
    }

    private fun writeDataInsideCsv(context: Context, csv: File, actionList: List<ActionsModel>) {
        csvWriter().open(csv, append = false) {
            writeRow(
                listOf(
                    "Nome da atividade",
                    "Carga horária",
                    "Data inicial",
                    "Data final",
                    "Descrição/Justificativa",
                    "Objetivos",
                    "Metodologia",
                    "Resultados/Produtos",
                    "Metodologia de avaliação",
                    "Avaliação",
                    "Avaliação da atividade"
                )
            )
            actionList.forEach {
                writeRow(
                    listOf(
                        it.title,
                        it.workload,
                        it.initialDate,
                        it.finalDate,
                        it.description,
                        it.goals,
                        it.methodology,
                        it.results,
                        it.evaluationMethodology,
                        it.actionEvaluation
                    )
                )
            }
        }
    }
}