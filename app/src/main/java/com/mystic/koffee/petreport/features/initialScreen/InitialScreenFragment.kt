package com.mystic.koffee.petreport.features.initialScreen

import android.os.Bundle
import android.view.View
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import com.mystic.koffee.petreport.MainActivity
import com.mystic.koffee.petreport.R
import com.mystic.koffee.petreport.databinding.FragmentInitialScreenBinding
import com.mystic.koffee.petreport.features.initialScreen.adapter.InitialScreenAdapter
import com.mystic.koffee.petreport.models.ReportsModel
import com.mystic.koffee.petreport.support.ui.AddReportDialog
import com.mystic.koffee.petreport.support.ui.GenericChoiceDialog
import com.mystic.koffee.petreport.support.utils.CreateSupportActionMode

class InitialScreenFragment : Fragment(R.layout.fragment_initial_screen) {


    /**
    Fragment life cycle
     **/
    private var _binding: FragmentInitialScreenBinding? = null
    private val binding get() = _binding!!
    private var actionMode: ActionMode? = null
    private lateinit var adapter: InitialScreenAdapter

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
        setupAdapter(MOCKY)
        setupAddFloatActionButton()
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

    /*
    * Action
    */
    private fun onLongClicked(position: Int) {
        enableActionMode(position)
    }

    private fun onItemClicked(position: Int) {
        enableActionMode(position)
    }

    private fun enableActionMode(position: Int) {
        actionMode = (activity as MainActivity).startSupportActionMode(
            CreateSupportActionMode(
                adapter,
                actionMode
            )
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

    private fun didClickedDeleteReport(reportId: Int) {
        //TODO DELETE FROM ROOM LIST
    }

    private fun didClickedShowReport(reportFile: ReportsModel) {
        navigateToReportDetails()
    }

    private fun addReport(title: String){
        MOCKY.add(ReportsModel(1,title))
        setupAdapter(MOCKY)
    }


    /* Dialogs and navigation */

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
        //TODO NAVIGATE FOR DETAILS REPORT
    }

    companion object {
        val MOCKY = mutableListOf<ReportsModel>()
    }
}