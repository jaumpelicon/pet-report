package com.mystic.koffee.petreport.features.actions.actionsReportScreen.adapter

import android.util.SparseBooleanArray
import android.view.ViewGroup
import androidx.core.util.isNotEmpty
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mystic.koffee.petreport.features.actions.addActionScreen.models.ActionsModel

class ActionsAdapter (
    private val listReports: MutableList<ActionsModel>,
    private val showReportCallback: (reportFile: ActionsModel) -> Unit,
    private val deleteCallback: (reportId: Long) -> Unit,
    private val onItemLongClick: (reportId: Int) -> Unit,
    private val onItemClick: (reportId: Int) -> Unit,
    private val dialogConfirmDelete: (acceptCallback: () -> Unit) -> Unit
) : RecyclerView.Adapter<ActionViewHolder>() {

    val selectedItems = SparseBooleanArray()
    private var currentSelectPosition: Int = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActionViewHolder {
        return ActionViewHolder.inflate(parent)
    }

    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        holder.bind(listReports[position], showReportCallback)
        setupClickedItemsViewHolder(holder, position)
    }

    private fun setupClickedItemsViewHolder(holder: ActionViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            if (selectedItems.isNotEmpty()) onItemClick.invoke(position)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick.invoke(position)
            return@setOnLongClickListener true
        }
        if (currentSelectPosition == position) currentSelectPosition = -1
    }

    override fun getItemCount(): Int = listReports.size

    fun toggleSelection(position: Int) {
        currentSelectPosition = position
        if (selectedItems[position, false]) {
            selectedItems.delete(position)
            listReports[position].selected = false
        } else {
            selectedItems.put(position, true)
            listReports[position].selected = true
        }
        notifyItemChanged(position)
    }

    fun deleteExams() {
        dialogConfirmDelete(::executeDelete)
    }

    private fun clearSelectedExams() {
        selectedItems.clear()
        listReports.filter { it.selected }.forEach {
            it.selected = false
        }
        notifyDataSetChanged()
    }

    private fun executeDelete() {
        listReports.filter { it.selected }.forEach { deleteCallback(it.id) }
        listReports.removeAll(listReports.filter { it.selected }.toSet())
        notifyDataSetChanged()
        currentSelectPosition = -1
        clearSelectedExams()
    }

    companion object DiffUtilCallback : DiffUtil.ItemCallback<ActionsModel>() {
        override fun areItemsTheSame(oldItem: ActionsModel, newItem: ActionsModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ActionsModel, newItem: ActionsModel) =
            oldItem.id == newItem.id
    }
}