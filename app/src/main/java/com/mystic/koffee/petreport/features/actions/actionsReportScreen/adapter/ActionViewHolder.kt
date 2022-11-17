package com.mystic.koffee.petreport.features.actions.actionsReportScreen.adapter

import android.content.Context
import android.content.res.ColorStateList
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
        context: Context,
    ) {
        with(binding) {
            titleActionCardTextView.text = action.title
            binding.seeDetailImageView.setOnClickListener {
                itemClickCallback(action)
            }
            val colorSelectedInt: Int = context.getColor(R.color.itemSelected)
            val cslSelected = ColorStateList.valueOf(colorSelectedInt)
            val colorBackgroundInt: Int = context.getColor(R.color.primaryBackGroundColor)
            val cslBackground = ColorStateList.valueOf(colorBackgroundInt)

            if (action.selected) {
                binding.itemReportCardView.backgroundTintList = cslSelected
            } else {
                binding.itemReportCardView.backgroundTintList = cslBackground
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