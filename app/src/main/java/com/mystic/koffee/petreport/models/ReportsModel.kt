package com.mystic.koffee.petreport.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports_model")
data class ReportsModel(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String,
    var date : String,
    var selected: Boolean = false
){
    constructor(title:String,date:String,selected: Boolean) : this(0,title,date,selected = false)
}
