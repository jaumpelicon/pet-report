package com.mystic.koffee.petreport.features.initialScreen

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.view.ActionMode
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.mystic.koffee.petreport.MainActivity
import com.mystic.koffee.petreport.R
import com.mystic.koffee.petreport.databinding.FragmentReportsBinding
import com.mystic.koffee.petreport.features.initialScreen.adapter.InitialScreenAdapter
import com.mystic.koffee.petreport.models.ReportsModel
import com.mystic.koffee.petreport.models.ViewState
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
    private lateinit var adapter: InitialScreenAdapter
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

    private fun setup() {
        setupAddFloatActionButton()
        setupRefreshState()
    }

    private fun setupAdapter(data: List<ReportsModel>) {
        adapter = InitialScreenAdapter(
            data as MutableList<ReportsModel>,
            ::didClickedShowReport,
            ::didClickedDeleteReport,
            ::onLongClicked,
            ::onItemClicked,
            ::showConfirmDeleteDialog
        )
        binding.listRecyclerView.adapter = adapter
    }

    private fun setupAddFloatActionButton() {
        binding.addReportFloatingActionButton.setOnClickListener {
            showAddReportDialog()
        }
    }

    private fun setupRefreshState() {
        binding.customSwipeRefresh.setOnRefreshListener { didRefresh() }
    }

    /*
    * Action
    */
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
        navigateToReportDetails()
    }

    private fun addReport(title: String) {
        val date = requireContext().getDate()
        val report = ReportsModel(title, date, false)
        viewModel.insertReport(report)
    }

    /* ObserveCoroutines */

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
                            is ViewState.Loading -> {
                                //TODO loading
                            }
                            is ViewState.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Error get list",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
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

    private fun observeInsertReports() {
        lifecycleScope.launch {
            viewModel.insertReportsState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { state ->
                    state?.let {
                        when (it) {
                            is ViewState.Success<*> -> {
                                Toast.makeText(requireContext(), "Inserido", Toast.LENGTH_SHORT)
                                    .show()
                                didRefresh()
                            }
                            is ViewState.Loading -> {
                                //TODO loading
                            }
                            is ViewState.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Error coroutines",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                }
        }
    }

    private fun observeDeleteReports() {
        lifecycleScope.launch {
            viewModel.deleteReportState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { state ->
                    state?.let {
                        when (it) {
                            is ViewState.Success<*> -> handleSucessDeleteReport()
                            is ViewState.Loading -> {
                                //TODO loading
                            }
                            is ViewState.Error -> handleErrorDeleteReport()
                        }
                    }
                }
        }
    }

    private fun handleSucessDeleteReport() {
        Toast.makeText(requireContext(), "Deletado com sucesso!", Toast.LENGTH_SHORT).show()
        didRefresh()
    }

    private fun handleErrorDeleteReport() {
        Toast.makeText(requireContext(), "Error get list", Toast.LENGTH_SHORT).show()
    }

    /* Dialogs and navigation and visibility functions */
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



    private fun navigateToReportDetails() {
        // TODO CALL NAVIGATE ACTION action_ReportsFragment_to_ActionsReportsFragment WITH ARGUMENTS
    }

}