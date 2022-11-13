package com.mystic.koffee.petreport.features.reportsScreen

import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import com.mystic.koffee.petreport.databinding.FragmentReportsBinding
import com.mystic.koffee.petreport.features.reportsScreen.adapter.ReportsScreenAdapter
import com.mystic.koffee.petreport.models.ReportsModel
import com.mystic.koffee.petreport.models.ViewState
import com.mystic.koffee.petreport.support.Constants.NAVIGATION_TITLE_ARGUMENTS
import com.mystic.koffee.petreport.support.extension.getDate
import com.mystic.koffee.petreport.support.ui.AddReportDialog
import com.mystic.koffee.petreport.support.ui.GenericChoiceDialog
import com.mystic.koffee.petreport.support.utils.CreateSupportActionMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReportsFragment : Fragment(R.layout.fragment_reports) {

    /**
    Fragment life cycle
     **/
    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!
    private var actionMode: ActionMode? = null
    private lateinit var adapter: ReportsScreenAdapter
    private val viewModel: ReportsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReportsBinding.bind(view)
        setup()
        observeCoroutines()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    /**
    Setup
     **/
    private fun setup() {
        setupAddFloatActionButton()
        setupRefreshState()
    }

    private fun setupAddFloatActionButton() {
        binding.addReportFloatingActionButton.setOnClickListener {
            showAddReportDialog()
        }
    }

    private fun setupRefreshState() {
        binding.customSwipeRefresh.setOnRefreshListener { didRefresh() }
    }

    private fun setupAdapter(data: List<ReportsModel>) {
        adapter = ReportsScreenAdapter(
            data as MutableList<ReportsModel>,
            ::didClickedShowReport,
            ::didClickedDeleteReport,
            ::onLongClicked,
            ::onItemClicked,
            ::showConfirmDeleteDialog
        )
        binding.listRecyclerView.adapter = adapter
    }

    /**
    Action
     **/
    private fun didRefresh() {
        changeRefreshVisibility(true)
        viewModel.getReports()
    }

    private fun onLongClicked(position: Int) {
        enableActionMode(position)
    }

    private fun onItemClicked(position: Int) {
        enableActionMode(position)
    }

    private fun enableActionMode(position: Int) {
        actionMode = (activity as MainActivity).startSupportActionMode(
            CreateSupportActionMode(adapter, actionMode)
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

    private fun didClickedDeleteReport(reportId: Long) {
        viewModel.deleteReport(reportId)
    }

    private fun didClickedShowReport(reportFile: ReportsModel) {
        navigateToReportDetails(reportFile.title)
    }

    private fun addReport(title: String) {
        val date = getDate()
        val report = ReportsModel(title, date, false)
        viewModel.insertReport(report)
    }

    /**
    ObserveCoroutines
     **/
    private fun observeCoroutines() {
        observeInsertReports()
        observeGetReports()
        observeDeleteReports()
    }

    private fun observeGetReports() {
        lifecycleScope.launch {
            viewModel.getReportsState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { state ->
                    state?.let {
                        when (it) {
                            is ViewState.Success<*> -> handleSuccessGetReports(it.data as MutableList<ReportsModel>)
                            is ViewState.Loading -> handleLoading()
                            is ViewState.Error -> handleErrorGetReports()
                        }
                    }
                }
        }
    }

    private fun handleSuccessGetReports(data: MutableList<ReportsModel>) {
        changeRefreshVisibility(false)
        if (data.isEmpty()) {
            showEmptyScreen(true)
        } else {
            showEmptyScreen(false)
            setupAdapter(data)
            changeRefreshVisibility(false)
        }
    }

    private fun handleErrorGetReports() {
        changeRefreshVisibility(false)
        Toast.makeText(requireContext(), "Error get list", Toast.LENGTH_SHORT).show()
    }

    private fun handleLoading() {
        changeRefreshVisibility(true)
    }

    private fun observeInsertReports() {
        lifecycleScope.launch {
            viewModel.insertReportsState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { state ->
                    state?.let {
                        when (it) {
                            is ViewState.Success<*> -> handleSuccessInsertReport()
                            is ViewState.Loading -> handleLoading() //TODO loading
                            is ViewState.Error -> handleErrorInsertReport()

                        }
                    }
                }
        }
    }

    private fun handleSuccessInsertReport() {
        didRefresh()
    }

    private fun handleErrorInsertReport() {
        changeRefreshVisibility(false)
        Toast.makeText(requireContext(), "Error insert item list", Toast.LENGTH_SHORT).show()
    }

    private fun observeDeleteReports() {
        lifecycleScope.launch {
            viewModel.deleteReportState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { state ->
                    state?.let {
                        when (it) {
                            is ViewState.Success<*> -> handleSuccessDeleteReport()
                            is ViewState.Loading -> {
                                //TODO loading
                            }
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

    /**
    Dialogs and navigation and visibility functions
     **/
    private fun showEmptyScreen(show: Boolean) {
        binding.listRecyclerView.isVisible = show.not()
        binding.emptyImageView.isVisible = show
        binding.emptyMessageTextView.isVisible = show
    }

    private fun changeRefreshVisibility(visible: Boolean) {
        binding.customSwipeRefresh.isRefreshing = visible
    }

    private fun showAddReportDialog() {
        AddReportDialog(addReportCallback = ::addReport).show(childFragmentManager, null)
    }

    private fun showConfirmDeleteDialog(acceptCallback: () -> Unit) {
        GenericChoiceDialog(
            descriptionText = getString(R.string.question_delete_exam_label),
            acceptCallback = { acceptCallback() }
        ).show(childFragmentManager, null)
    }

    private fun navigateToReportDetails(titleArguments: String) {
        val arguments = bundleOf(NAVIGATION_TITLE_ARGUMENTS to titleArguments)
        findNavController().navigate(R.id.action_ReportsFragment_to_ActionsReportsFragment, arguments)
    }
}