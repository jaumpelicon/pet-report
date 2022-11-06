package com.mystic.koffee.petreport.support.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mystic.koffee.petreport.databinding.GenericChoiceDialogBinding

class GenericChoiceDialog(
    private val descriptionText: String,
    private val acceptCallback: () -> Unit,
    private val declineCallback: (() -> Unit)? = null
) : DialogFragment() {

    private var _binding: GenericChoiceDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GenericChoiceDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setup() {
        setupNegativeButton()
        setupPositiveButton()
        setupDialogTitle()
    }

    private fun setupDialogTitle() {
        binding.confirmationMessageRemoveReadingsTextView.text = descriptionText
    }

    private fun setupPositiveButton() {
        binding.yesConfirmationButton.setOnClickListener {
            acceptCallback()
            dismiss()
        }
    }

    private fun setupNegativeButton() {
        binding.notConfirmationButton.setOnClickListener {
            declineCallback?.invoke()
            dismiss()
        }
    }
}