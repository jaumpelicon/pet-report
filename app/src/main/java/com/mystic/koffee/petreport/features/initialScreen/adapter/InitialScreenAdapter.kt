package com.mystic.koffee.petreport.features.initialScreen.adapter

import android.util.SparseBooleanArray
import android.view.ViewGroup
import androidx.core.util.isNotEmpty
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mystic.koffee.petreport.models.ReportsModel

class InitialScreenAdapter (
    private val listReports: MutableList<ReportsModel>,
    private val showReportCallback: (reportFile: ReportsModel) -> Unit,
    private val deleteCallback: (reportId: Int) -> Unit,
    private val onItemLongClick: (reportId: Int) -> Unit,
    private val onItemClick: (reportId: Int) -> Unit,
    private val dialogConfirmDelete: (acceptCallback: () -> Unit) -> Unit
) : RecyclerView.Adapter<InitialScreenViewHolder>() {

    val selectedItems = SparseBooleanArray()
    private var currentSelectPosition: Int = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InitialScreenViewHolder {
        return InitialScreenViewHolder.inflate(parent)
    }

    override fun onBindViewHolder(holder: InitialScreenViewHolder, position: Int) {
        holder.bind(listReports[position], showReportCallback)
        setupClickedItemsViewHolder(holder, position)
    }

    private fun setupClickedItemsViewHolder(holder: InitialScreenViewHolder, position: Int) {
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
        listReports.removeAll(listReports.filter { it.selected })
        notifyDataSetChanged()
        currentSelectPosition = -1
        clearSelectedExams()
    }

    companion object DiffUtilCallback : DiffUtil.ItemCallback<ReportsModel>() {
        override fun areItemsTheSame(oldItem: ReportsModel, newItem: ReportsModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ReportsModel, newItem: ReportsModel) =
            oldItem.id == newItem.id
    }
}