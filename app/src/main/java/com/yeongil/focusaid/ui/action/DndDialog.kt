package com.yeongil.focusaid.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.focusaid.databinding.DialogDndBinding
import com.yeongil.focusaid.utils.navigateSafe
import com.yeongil.focusaid.viewModel.viewModel.action.DndActionViewModel
import com.yeongil.focusaid.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.focusaid.viewModelFactory.RuleEditViewModelFactory

class DndDialog : BottomSheetDialogFragment() {
    private var _binding: DialogDndBinding? = null
    private val binding get() = _binding!!

    private val directions = DndDialogDirections

    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        RuleEditViewModelFactory(requireContext())
    }
    private val dndActionViewModel by activityViewModels<DndActionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogDndBinding.inflate(inflater, container, false)

        binding.cancelBtn.setOnClickListener {
            val goToEditFragment = ruleEditViewModel.editingRule.value?.dndAction == null
            if (goToEditFragment) {
                findNavController().navigateSafe(directions.actionDndDialogToActionEditFragment())
            } else {
                findNavController().navigateSafe(directions.actionGlobalActionFragment())
            }
        }
        binding.completeBtn.setOnClickListener {
            ruleEditViewModel.addTriggerAction(dndActionViewModel.getDndAction())
            findNavController().navigateSafe(directions.actionGlobalActionFragment())
        }

        return binding.root
    }
}