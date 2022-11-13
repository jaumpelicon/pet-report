package com.mystic.koffee.petreport.features.actionsReport

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mystic.koffee.petreport.R
import com.mystic.koffee.petreport.databinding.FragmentActionsReportBinding
import com.mystic.koffee.petreport.databinding.FragmentReportsBinding

class ActionsReportFragment : Fragment(R.layout.fragment_actions_report) {

    private var _binding : FragmentActionsReportBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentActionsReportBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}