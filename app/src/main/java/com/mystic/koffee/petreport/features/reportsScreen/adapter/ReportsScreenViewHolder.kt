package com.mystic.koffee.petreport.features.reportsScreen.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mystic.koffee.petreport.R
import com.mystic.koffee.petreport.databinding.ReportItemCardBinding
import com.mystic.koffee.petreport.features.reportsScreen.models.ReportsModel

class ReportsScreenViewHolder(
    private val binding: ReportItemCardBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        report: ReportsModel,
        itemClickCallback: (report: ReportsModel) -> Unit,
        context: Context
    ) {
        with(binding) {
            titleReportCardTextView.text = report.title
            binding.generalConstraintLayout.setOnClickListener {
                itemClickCallback(report)
            }
            val colorSelectedInt: Int = context.getColor(R.color.itemSelected)
            val cslSelected = ColorStateList.valueOf(colorSelectedInt)
            val colorBackgroundInt: Int = context.getColor(R.color.primaryBackGroundColor)
            val cslBackground = ColorStateList.valueOf(colorBackgroundInt)
            if (report.selected) {
                itemReportCardView.backgroundTintList = cslSelected
            } else {
                itemReportCardView.backgroundTintList = cslBackground
            }
        }
    }

    companion object {
        fun inflate(parent: ViewGroup) = ReportsScreenViewHolder(
            ReportItemCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}