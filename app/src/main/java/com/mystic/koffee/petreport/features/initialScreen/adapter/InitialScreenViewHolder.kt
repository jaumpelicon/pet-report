package com.mystic.koffee.petreport.features.initialScreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mystic.koffee.petreport.R
import com.mystic.koffee.petreport.databinding.ReportItemCardBinding
import com.mystic.koffee.petreport.models.ReportsModel

class InitialScreenViewHolder(
    private val binding: ReportItemCardBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        report: ReportsModel,
        itemClickCallback: (report: ReportsModel) -> Unit,
    ) {
        with(binding) {
            titleReportCardTextView.text = report.title
            itemReportCardView.setOnClickListener {
                itemClickCallback(report)
            }
            if (report.selected) {
                binding.itemReportCardView.setBackgroundResource(R.color.itemSelected)
            } else {
                binding.itemReportCardView.setBackgroundResource(R.color.white)
            }
        }
    }

    companion object {
        fun inflate(parent: ViewGroup) = InitialScreenViewHolder(
            ReportItemCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}