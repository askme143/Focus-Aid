package com.yeongil.digitalwellbeing.ui.confirm

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.digitalwellbeing.background.MainService
import com.yeongil.digitalwellbeing.databinding.DialogConfirmBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.DescriptionViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.DescriptionViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class ConfirmDialog : BottomSheetDialogFragment() {
    private var _binding: DialogConfirmBinding? = null
    private val binding get() = _binding!!

    private val directions = ConfirmDialogDirections

    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        RuleEditViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogConfirmBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = ruleEditViewModel

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionConfirmDialogToConfirmFragment())
        }
        binding.completeBtn.setOnClickListener { onComplete() }
        binding.ruleName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == KeyEvent.KEYCODE_ENDCALL) {
                onComplete(); false
            } else true
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
        ruleEditViewModel.saveRule()

        val rule = ruleEditViewModel.editingRule.value!!
        val intent = Intent(requireContext(), MainService::class.java)
        intent.action = MainService.RULE_CHANGE
        intent.putExtra(MainService.CHANGED_RULE_ID_KEY, rule.ruleInfo.ruleId)
        requireActivity().startService(intent)

        Toast.makeText(requireContext().applicationContext, "규칙이 생성되었습니다.", Toast.LENGTH_SHORT)
            .show()
        findNavController().navigateSafe(directions.actionConfirmDialogToMainFragment())
    }
}