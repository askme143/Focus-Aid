package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.FragmentAppBlockActionBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.AppBlockActionViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.AppBlockEntryViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.AppListViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.AppBlockActionViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.AppListViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory

class AppBlockActionFragment : Fragment() {
    private var _binding: FragmentAppBlockActionBinding? = null
    private val binding get() = _binding!!

    private val directions = AppBlockActionFragmentDirections

    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        RuleEditViewModelFactory(requireContext())
    }
    private val appBlockActionViewModel by activityViewModels<AppBlockActionViewModel> {
        AppBlockActionViewModelFactory(requireContext())
    }
    private val appBlockEntryViewModel by activityViewModels<AppBlockEntryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppBlockActionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = appBlockActionViewModel

        initViewModel()

        appBlockActionViewModel.itemClickEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { packageName ->
                val entry = appBlockActionViewModel.appBlockEntryList.value!!
                    .filter { it.packageName == packageName }[0]
                appBlockEntryViewModel.init(entry)
                findNavController().navigateSafe(directions.actionAppBlockActionFragmentToAppBlockEntryDialog())
            }
        }
        appBlockActionViewModel.itemClickDeleteEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { packageName ->
                with(appBlockActionViewModel.appBlockEntryList) {
                    val oldEntryList = this.value ?: listOf()
                    val index = oldEntryList.map { it.packageName }.indexOf(packageName)
                    this.value = oldEntryList - oldEntryList[index]
                }
            }
        }

        binding.addBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionAppBlockActionFragmentToAppBlockListFragment())
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) { onStartGoBack() }
        binding.beforeBtn.setOnClickListener { onStartGoBack() }
        binding.completeBtn.setOnClickListener {
            appBlockActionViewModel.editing = false
            ruleEditViewModel.addTriggerAction(appBlockActionViewModel.getAppBlockAction())
            findNavController().navigateSafe(directions.actionGlobalActionFragment())
        }

        return binding.root
    }

    private fun initViewModel() {
        if (!appBlockActionViewModel.editing) {
            val action = ruleEditViewModel.editingRule.value?.appBlockAction
            if (action != null) {
                appBlockActionViewModel.init(action)
            } else appBlockActionViewModel.init()
        }
    }

    private fun onStartGoBack() {
        if (appBlockActionViewModel.appBlockEntryList.value?.isNotEmpty() == true) {
            val bottomSheetDialog = BottomSheetDialog(requireContext())
            bottomSheetDialog.setContentView(R.layout.dialog_cancel_confirm)
            bottomSheetDialog.show()

            bottomSheetDialog.findViewById<Button>(R.id.complete_btn)!!
                .setOnClickListener {
                    bottomSheetDialog.dismiss()
                    goBack()
                }
            bottomSheetDialog.findViewById<Button>(R.id.cancel_btn)!!
                .setOnClickListener { bottomSheetDialog.dismiss() }
        } else {
            goBack()
        }
    }

    private fun goBack() {
        appBlockActionViewModel.editing = false
        val goToEditFragment = ruleEditViewModel.editingRule.value?.appBlockAction == null
        if (goToEditFragment) {
            findNavController().navigateSafe(directions.actionAppBlockActionFragmentToActionEditFragment())
        } else {
            findNavController().navigateSafe(directions.actionGlobalActionFragment())
        }
    }
}