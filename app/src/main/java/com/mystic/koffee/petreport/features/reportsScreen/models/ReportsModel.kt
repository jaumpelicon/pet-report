package com.mystic.koffee.petreport.features.reportsScreen.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "reports")
data class ReportsModel(
    @PrimaryKey(autoGenerate = true) val reportId: Long,
    val title: String,
    var date: String,
    var selected: Boolean = false
): Parcelable {
    constructor(title: String, date: String) :
            this(0, title, date)
}
