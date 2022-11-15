package com.mystic.koffee.petreport.api.dao.setup

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mystic.koffee.petreport.api.dao.repositorys.ActionsDao
import com.mystic.koffee.petreport.api.dao.repositorys.ReportsDao
import com.mystic.koffee.petreport.features.actions.addActionScreen.models.ActionsModel
import com.mystic.koffee.petreport.features.reportsScreen.models.ReportsModel

@Database(
    version = 1,
    entities = [ReportsModel::class, ActionsModel::class],
    autoMigrations = [AutoMigration(from = 1, to = 2)],
    exportSchema = true
)
abstract class PetReportRoom : RoomDatabase() {
    abstract val reportDao: ReportsDao
    abstract val actionsDao: ActionsDao

    companion object {

        @Volatile
        private var INSTANCE: PetReportRoom? = null

        fun getInstance(context: Context): PetReportRoom {

            synchronized(this) {

                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PetReportRoom::class.java,
                        "pet_report_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
