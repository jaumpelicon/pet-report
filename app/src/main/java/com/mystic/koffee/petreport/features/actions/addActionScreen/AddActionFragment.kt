package com.mystic.koffee.petreport.features.actions.addActionScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.mystic.koffee.petreport.R
import com.mystic.koffee.petreport.databinding.FragmentAddActionBinding
import com.mystic.koffee.petreport.features.actions.ActionsViewModel
import com.mystic.koffee.petreport.features.actions.addActionScreen.models.ActionsModel
import com.mystic.koffee.petreport.models.ViewState
import com.mystic.koffee.petreport.support.Constants.ACTION_ID_ARGUMENTS
import com.mystic.koffee.petreport.support.Constants.NAVIGATION_ID_ARGUMENTS
import com.mystic.koffee.petreport.support.extension.hideKeyboard
import com.mystic.koffee.petreport.support.extension.toString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.properties.Delegates

@AndroidEntryPoint
class AddActionFragment : Fragment(R.layout.fragment_add_action) {

    private var _binding: FragmentAddActionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ActionsViewModel by viewModels()
    private var reportId by Delegates.notNull<Long>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddActionBinding.bind(view)
        setup()
        observeCoroutines()
    }

    private fun setup() {
        setupBackButton()
        setupInitialDateTextView()
        setupFinalDateTextView()
        setupHideKeyboard()
        setupMaterialAutoComplete()
        setupAddActionButton()
        setupHasActionCreated()
    }

    private fun setupHasActionCreated() {
        //TODO IF NOT NULL ENABLE EDIT
        val action = arguments?.getParcelable<ActionsModel>(ACTION_ID_ARGUMENTS)
        if (action!= null){
            binding.titleActionTextInputEditText.setText(action.title)
            binding.hoursActionTextInputEditText.setText(action.workload)
            binding.initialDateTextView.text = action.initialDate
            binding.finalDateTextView.text = action.finalDate
            binding.descriptionTextInputEditText.setText(action.description)
            binding.goalsTextInputEditText.setText(action.goals)
            binding.methodologyTextInputEditText.setText(action.methodology)
            binding.resultsTextInputEditText.setText(action.results)
            binding.evaluationMethodologyTextInputEditText.setText(action.evaluationMethodology)
            binding.evaluationActionTextInputEditText.setText(action.actionEvaluation)
        }
    }

    private fun setupMaterialAutoComplete() {
        val optionsEvaluated = resources.getStringArray(R.array.options_evaluated)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1, optionsEvaluated
        )
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
        binding.initialDateTextView.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(childFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener { date ->
                val data = Instant.ofEpochMilli(date)
                    .atZone(ZoneId.of("America/Sao_Paulo"))
                    .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
                    .toLocalDate()
                binding.initialDateTextView.text = data.toString("dd/MM/yyyy")
            }
        }
    }

    private fun setupFinalDateTextView() {
        binding.finalDateTextView.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(childFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener { date ->
                val data = Instant.ofEpochMilli(date)
                    .atZone(ZoneId.of("America/Sao_Paulo"))
                    .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
                    .toLocalDate()
                binding.finalDateTextView.text = data.toString("dd/MM/yyyy")
            }
        }
    }

    private fun setupAddActionButton() {
        binding.addActionButton.setOnClickListener {
            addAction()
        }
    }

    private fun addAction() {
        //TODO VALIDATE IF NOT EMPTY
        arguments?.getLong(NAVIGATION_ID_ARGUMENTS)?.let {
            reportId = it
        }
        val action = ActionsModel(
            reportId,
            binding.titleActionTextInputEditText.text.toString(),
            binding.hoursActionTextInputEditText.text.toString(),
            binding.initialDateTextView.text.toString(),
            binding.finalDateTextView.text.toString(),
            binding.descriptionTextInputEditText.text.toString(),
            binding.goalsTextInputEditText.text.toString(),
            binding.methodologyTextInputEditText.text.toString(),
            binding.resultsTextInputEditText.text.toString(),
            binding.evaluationMethodologyTextInputEditText.text.toString(),
            binding.evaluationActionTextInputEditText.text.toString()
        )
        viewModel.insertAction(action)
    }

    private fun observeCoroutines() {
        observeInsertReports()
    }

    private fun observeInsertReports() {
        lifecycleScope.launch {
            viewModel.insertActionsState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { state ->
                    state?.let {
                        when (it) {
                            is ViewState.Success<*> -> handleSuccessInsertAction()
                            is ViewState.Error -> handleErrorInsertAction()
                        }
                    }
                }
        }
    }

    private fun handleSuccessInsertAction() {
        val arguments = bundleOf(NAVIGATION_ID_ARGUMENTS to reportId)
        findNavController().navigate(R.id.action_addActionFragment_to_ActionsReportsFragment,arguments)
    }

    private fun handleErrorInsertAction() {
        Toast.makeText(requireContext(), "Error insert item list", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupHideKeyboard() {
        binding.defaultContraintLayout.setOnTouchListener { view, _ ->
            view?.let { requireContext().hideKeyboard(it) }
            true
        }
    }
}