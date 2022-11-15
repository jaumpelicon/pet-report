package com.mystic.koffee.petreport.features.addActionScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.mystic.koffee.petreport.R
import com.mystic.koffee.petreport.databinding.FragmentAddActionBinding
import com.mystic.koffee.petreport.support.extension.hideKeyboard
import com.mystic.koffee.petreport.support.extension.toString
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset

@AndroidEntryPoint
class AddActionFragment : Fragment(R.layout.fragment_add_action) {

    private var _binding: FragmentAddActionBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddActionBinding.bind(view)
        setup()
    }

    private fun setup() {
        setupBackButton()
        setupInitialDateTextView()
        setupFinalDateTextView()
        setupHideKeyboard()
        setupMaterialAutoComplete()
    }

    private fun setupMaterialAutoComplete() {
        val optionsEvaluated = resources.getStringArray(R.array.options_evaluated)
        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_list_item_1, optionsEvaluated)
        binding.evaluationAutoCompleteTextView.setAdapter(adapter)
    }

    private fun setupBackButton() {
        binding.backButton.setCompoundDrawablesWithIntrinsicBounds(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.ic_baseline_arrow_back_ios_24
            ), null, null, null
        )
        binding.backButton.setOnClickListener { findNavController().popBackStack() }
    }

    private fun setupInitialDateTextView() {
        binding.initialDateFilterTextView.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(childFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener { date ->
                val data = Instant.ofEpochMilli(date)
                    .atZone(ZoneId.of("America/Sao_Paulo"))
                    .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
                    .toLocalDate()
                binding.initialDateFilterTextView.text = data.toString("dd/MM/yyyy")
            }
        }
    }

    private fun setupFinalDateTextView() {
        binding.finalDateFilterTextView.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(childFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener { date ->
                val data = Instant.ofEpochMilli(date)
                    .atZone(ZoneId.of("America/Sao_Paulo"))
                    .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
                    .toLocalDate()
                binding.finalDateFilterTextView.text = data.toString("dd/MM/yyyy")
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupHideKeyboard() {
        binding.defaultContraintLayout.setOnTouchListener { view, _ ->
            view?.let { requireContext().hideKeyboard(it) }
            true
        }
    }
}