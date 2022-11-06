package com.mystic.koffee.petreport.support.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mystic.koffee.petreport.databinding.AddReportDialogBinding

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