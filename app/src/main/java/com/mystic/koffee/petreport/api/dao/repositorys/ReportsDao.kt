package com.mystic.koffee.petreport.api.dao.repositorys

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.mystic.koffee.petreport.features.reportsScreen.models.ReportsModel

@Dao
interface ReportsDao {

    @Transaction
    @Insert
    fun insertReport(report: ReportsModel)

    @Transaction
    @Query("DELETE FROM reports WHERE reportId = :reportId")
    suspend fun removeReport(reportId: Long): Int

    @Transaction
    @Query("SELECT * FROM reports ORDER BY strftime(date) DESC")
    fun getAllReports(): List<ReportsModel>


    @Query("DELETE FROM actions WHERE fkReportId = :reportId")
    fun cleanActionsByReport(reportId: Long)

    @Query("DELETE FROM reports")
    fun dropReportsBank()
}