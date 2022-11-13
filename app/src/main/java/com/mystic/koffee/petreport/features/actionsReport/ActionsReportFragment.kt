package com.mystic.koffee.petreport.features.actionsReport

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mystic.koffee.petreport.R
import com.mystic.koffee.petreport.databinding.FragmentActionsReportBinding
import com.mystic.koffee.petreport.support.Constants.NAVIGATION_TITLE_ARGUMENTS

class ActionsReportFragment : Fragment(R.layout.fragment_actions_report) {

    private var _binding: FragmentActionsReportBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentActionsReportBinding.bind(view)
        setup()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setup() {
        setupTitle()
        setupBackButton()
        setupFloatActionButton()
    }

    private fun setupTitle() {
        binding.titleActionsReportTextView.text = arguments?.getString(NAVIGATION_TITLE_ARGUMENTS)
    }

    private fun setupBackButton() {
        binding.backButton.setCompoundDrawablesWithIntrinsicBounds(
            getDrawable(
                requireContext(),
                R.drawable.ic_baseline_arrow_back_ios_24
            ), null, null, null
        )
        binding.backButton.setOnClickListener { findNavController().popBackStack() }
    }

    private fun setupFloatActionButton() {
        //TODO NAVIGATE FOR CREATE ACTION
    }
}