package com.mystic.koffee.petreport.features.actions.addActionScreen.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actions_model")
data class ActionsModel(
    @PrimaryKey(autoGenerate = true) val id: Long,
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
) {
    constructor(
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