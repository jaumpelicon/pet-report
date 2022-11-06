package com.mystic.koffee.petreport.support.utils

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.ActionMode
import com.mystic.koffee.petreport.R
import com.mystic.koffee.petreport.features.initialScreen.adapter.InitialScreenAdapter

class CreateSupportActionMode(
    private val adapter: InitialScreenAdapter,
    private var actionMode: ActionMode?
) : ActionMode.Callback {

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.menu_delete_recycler, menu)
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_delete_action) {
            adapter.deleteExams()
            mode?.finish()
            return true
        }
        return false
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        actionMode = null
    }
}