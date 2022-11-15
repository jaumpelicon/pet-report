package com.mystic.koffee.petreport.api.dao.repositorys

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.mystic.koffee.petreport.features.actions.addActionScreen.models.ActionsModel

@Dao
interface ActionsDao {

    @Transaction
    @Insert
    fun insertAction(action: ActionsModel)

    @Transaction
    @Query("DELETE FROM actions_model WHERE id = :actionId")
    suspend fun removeAction(actionId: Long): Int

    @Transaction
    @Query("SELECT * FROM actions_model ORDER BY strftime(initialDate) DESC")
    fun getAllActions(): List<ActionsModel>
}