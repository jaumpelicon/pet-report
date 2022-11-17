package com.mystic.koffee.petreport.support.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.mystic.koffee.petreport.databinding.AddReportDialogBinding
import com.mystic.koffee.petreport.support.extension.setWidthPercent

class AddReportDialog (
    private val addReportCallback: (title: String) ->  Unit,
) : DialogFragment() {
    private lateinit var binding: AddReportDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddReportDialogBinding.inflate(inflater, container, false)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
        this.setWidthPercent(50)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    private fun setup() {
        with(binding) {
            addReportButton.setOnClickListener {
                addReportCallback(binding.signatureCodeTextInputEditText.text.toString())
                dismiss()
            }
        }
    }
}