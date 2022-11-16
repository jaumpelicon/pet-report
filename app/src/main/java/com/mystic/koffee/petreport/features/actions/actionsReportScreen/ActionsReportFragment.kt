package com.mystic.koffee.petreport.features.actions.actionsReportScreen

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.appcompat.view.ActionMode
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mystic.koffee.petreport.MainActivity
import com.mystic.koffee.petreport.R
import com.mystic.koffee.petreport.databinding.FragmentActionsReportBinding
import com.mystic.koffee.petreport.features.actions.ActionsViewModel
import com.mystic.koffee.petreport.features.actions.actionsReportScreen.adapter.ActionsAdapter
import com.mystic.koffee.petreport.features.actions.addActionScreen.AddActionFragment
import com.mystic.koffee.petreport.features.actions.addActionScreen.models.ActionsModel
import com.mystic.koffee.petreport.models.ViewState
import com.mystic.koffee.petreport.support.Constants
import com.mystic.koffee.petreport.support.Constants.NAVIGATION_ID_ARGUMENTS
import com.mystic.koffee.petreport.support.Constants.NAVIGATION_TITLE_ARGUMENTS
import com.mystic.koffee.petreport.support.ui.GenericChoiceDialog
import com.mystic.koffee.petreport.support.utils.CreateSupportAction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ActionsReportFragment : Fragment(R.layout.fragment_actions_report) {

    private var _binding: FragmentActionsReportBinding? = null
    private val binding get() = _binding!!
    private var actionMode: ActionMode? = null
    private lateinit var adapter: ActionsAdapter
    private val viewModel: ActionsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentActionsReportBinding.bind(view)
        setup()
        observeCoroutines()
    }

    override fun onStart() {
        super.onStart()
        didRefresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setup() {
        setupTitle()
        setupBackButton()
        setupFloatActionButton()
        setupRefreshState()
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
        binding.backButton.setOnClickListener { findNavController().navigate(R.id.action_ActionsReportsFragment_to_ReportsFragment) }
    }

    private fun setupFloatActionButton() {
        val reportId =
            bundleOf(NAVIGATION_ID_ARGUMENTS to arguments?.getLong(NAVIGATION_ID_ARGUMENTS))
        binding.addActionFloatingActionButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_ActionsReportsFragment_to_addActionFragment2,
                reportId
            )
        }
    }

    private fun setupRefreshState() {
        binding.customSwipeRefresh.setOnRefreshListener { didRefresh() }
    }

    private fun setupAdapter(actionsList: MutableList<ActionsModel>) {
        adapter = ActionsAdapter(
            actionsList,
            ::didClickedShowReport,
            ::didClickedDeleteReport,
            ::onLongClicked,
            ::onItemClicked,
            ::showConfirmDeleteDialog
        )
        binding.listRecyclerView.adapter = adapter
    }

    private fun onLongClicked(position: Int) {
        enableActionMode(position)
    }

    private fun onItemClicked(position: Int) {
        enableActionMode(position)
    }

    private fun enableActionMode(position: Int) {
        actionMode = (activity as MainActivity).startSupportActionMode(
            CreateSupportAction(adapter, actionMode)
        )
        didSelectedItems(position)
    }

    private fun didSelectedItems(position: Int) {
        adapter.toggleSelection(position)
        val size = adapter.selectedItems.size()
        if (size != 0) {
            actionMode?.title =
                String.format(getString(R.string.select_items_delete_size_label), size)
            actionMode?.invalidate()
            return
        }
        actionMode?.finish()
    }

    private fun didRefresh() {
        changeRefreshVisibility(true)
        arguments?.getLong(NAVIGATION_ID_ARGUMENTS)?.let { viewModel.getActions(it) }
    }

    private fun didClickedDeleteReport(actionId: Long) {
        viewModel.deleteAction(actionId)
    }

    private fun didClickedShowReport(actionFile: ActionsModel) {
        val args = Bundle()
        args.putParcelable(Constants.ACTION_ID_ARGUMENTS, actionFile)
        navigateToReportDetails(args)
    }

    private fun navigateToReportDetails(arguments: Bundle) {
        findNavController().navigate(R.id.action_ActionsReportsFragment_to_addActionFragment2,arguments)
    }

    private fun observeCoroutines() {
        observeGetActions()
        observeDeleteActions()
    }

    private fun observeGetActions() {
        lifecycleScope.launch {
            viewModel.getActionsState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { state ->
                    state?.let {
                        when (it) {
                            is ViewState.Success<*> -> handleSuccessGetActions(it.data as MutableList<ActionsModel>)
                            is ViewState.Loading -> handleLoading()
                            is ViewState.Error -> handleErrorGetActions()
                        }
                    }
                }
        }
    }

    private fun handleSuccessGetActions(actionsList: MutableList<ActionsModel>) {
        if (actionsList.isEmpty()) {
            showEmptyScreen(true)
            changeRefreshVisibility(false)
        } else {
            showEmptyScreen(false)
            setupAdapter(actionsList)
            changeRefreshVisibility(false)
        }
    }

    private fun handleLoading() {
        changeRefreshVisibility(true)
    }

    private fun handleErrorGetActions() {
        Toast.makeText(requireContext(), "Error get actions", Toast.LENGTH_SHORT).show()
    }

    private fun observeDeleteActions() {
        lifecycleScope.launch {
            viewModel.deleteActionState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { state ->
                    state?.let {
                        when (it) {
                            is ViewState.Success<*> -> handleSuccessDeleteReport()
                            is ViewState.Loading -> handleLoading()
                            is ViewState.Error -> handleErrorDeleteReport()
                        }
                    }
                }
        }
    }

    private fun handleSuccessDeleteReport() {
        didRefresh()
    }

    private fun handleErrorDeleteReport() {
        changeRefreshVisibility(false)
        Toast.makeText(requireContext(), "Error delete item list", Toast.LENGTH_SHORT).show()
    }

    private fun showConfirmDeleteDialog(acceptCallback: () -> Unit) {
        GenericChoiceDialog(
            descriptionText = getString(R.string.question_delete_exam_label),
            acceptCallback = { acceptCallback() }
        ).show(childFragmentManager, null)
    }

    private fun showEmptyScreen(show: Boolean) {
        binding.listRecyclerView.isVisible = show.not()
        binding.emptyImageView.isVisible = show
        binding.emptyMessageTextView.isVisible = show
    }

    private fun changeRefreshVisibility(visible: Boolean) {
        binding.customSwipeRefresh.isRefreshing = visible
    }
}
