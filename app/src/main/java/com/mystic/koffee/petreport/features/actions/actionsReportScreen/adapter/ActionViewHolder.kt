package com.mystic.koffee.petreport.features.actions.actionsReportScreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mystic.koffee.petreport.R
import com.mystic.koffee.petreport.databinding.ActionReportItemCardBinding
import com.mystic.koffee.petreport.features.actions.addActionScreen.models.ActionsModel

class ActionViewHolder (
    private val binding: ActionReportItemCardBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        action: ActionsModel,
        itemClickCallback: (report: ActionsModel) -> Unit,
    ) {
        with(binding) {
            titleActionCardTextView.text = action.title
            seeDetailImageView.setOnClickListener {
                itemClickCallback(action)
            }
            if (action.selected) {
                binding.itemReportCardView.setBackgroundResource(R.color.itemSelected)
            } else {
                binding.itemReportCardView.setBackgroundResource(R.color.white)
            }
        }
    }

    companion object {
        fun inflate(parent: ViewGroup) = ActionViewHolder(
            ActionReportItemCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}