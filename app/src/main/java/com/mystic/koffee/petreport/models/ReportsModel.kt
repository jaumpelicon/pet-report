package com.mystic.koffee.petreport.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReportsModel(
    val id: Int,
    val title: String,
    var selected: Boolean = false
)
