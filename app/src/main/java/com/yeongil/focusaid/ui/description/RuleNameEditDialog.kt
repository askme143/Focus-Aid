package com.yeongil.focusaid.ui.description

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.focusaid.background.MainService
import com.yeongil.focusaid.databinding.DialogRuleNameEditBinding
import com.yeongil.focusaid.utils.navigateSafe
import com.yeongil.focusaid.viewModel.viewModel.rule.DescriptionViewModel
import com.yeongil.focusaid.viewModelFactory.DescriptionViewModelFactory

class RuleNameEditDialog : BottomSheetDialogFragment() {
    private var _binding: DialogRuleNameEditBinding? = null
    private val binding get() = _binding!!

    private val directions = RuleNameEditDialogDirections

    private val descriptionViewModel by activityViewModels<DescriptionViewModel> {
        DescriptionViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRuleNameEditBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = descriptionViewModel

        descriptionViewModel.refreshEditingRuleName()

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionRuleNameEditDialogToDescriptionFragment())
        }
        binding.completeBtn.setOnClickListener { descriptionViewModel.updateRuleName() }
        binding.ruleName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == KeyEvent.KEYCODE_ENDCALL) {
                descriptionViewModel.updateRuleName()
                false
            } else true
        }

        descriptionViewModel.nameUpdateEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { onComplete() }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.ruleName.requestFocus()
        binding.ruleName.selectAll()
        val inputMethodManager =
            dialog?.context?.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.ruleName, 0)
    }

    private fun onComplete() {
        /* Notify rule change */
        val intent = Intent(requireContext(), MainService::class.java)
        intent.action = MainService.RULE_CHANGE
        requireActivity().startService(intent)
        /* Navigate */
        findNavController().navigateSafe(directions.actionRuleNameEditDialogToDescriptionFragment())
    }
}