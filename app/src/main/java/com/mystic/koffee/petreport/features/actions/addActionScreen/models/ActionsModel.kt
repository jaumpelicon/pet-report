package com.mystic.koffee.petreport.features.actions.addActionScreen.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.mystic.koffee.petreport.features.reportsScreen.models.ReportsModel
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "actions")
data class ActionsModel(
    @PrimaryKey(autoGenerate = true) val actionId: Long,
    val fkReportId: Long,
    val title: String,
    var workload: String,
    var initialDate: String,
    var finalDate: String,
    var description: String,
    var goals: String,
    var methodology: String,
    var results: String,
    var evaluationMethodology: String,
    var actionEvaluation: String,
    var selected: Boolean = false
) : Parcelable {
    constructor(
        fkReportId: Long,
        title: String,
        workload: String,
        initialDate: String,
        finalDate: String,
        description: String,
        goals: String,
        methodology: String,
        results: String,
        evaluationMethodology: String,
        actionEvaluation: String,
    ) : this(
        0,
        fkReportId,
        title,
        workload,
        initialDate,
        finalDate,
        description,
        goals,
        methodology,
        results,
        evaluationMethodology,
        actionEvaluation
    )
}