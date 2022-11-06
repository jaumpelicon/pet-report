package com.mystic.koffee.petreport.features.initialScreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mystic.koffee.petreport.R
import com.mystic.koffee.petreport.databinding.FragmentInitialScreenBinding
import com.mystic.koffee.petreport.support.ui.AddReportDialog

class InitialScreenFragment : Fragment(R.layout.fragment_initial_screen) {


    /**
    Fragment life cycle
     **/
    private var _binding: FragmentInitialScreenBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentInitialScreenBinding.bind(view)
        setup()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setup() {
        setupAddFloatActionButton()
    }

    private fun setupAddFloatActionButton() {
        binding.addReportFloatingActionButton.setOnClickListener {
            showAddReportDialog()
        }
    }

    private fun showAddReportDialog() {
        childFragmentManager.let {
            AddReportDialog(addReportCallback = { print("teste") }).show(it, null)
        }
    }
}