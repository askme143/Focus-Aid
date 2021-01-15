package com.yeongil.digitalwellbeing.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeongil.digitalwellbeing.data.dao.RuleDao
import com.yeongil.digitalwellbeing.data.database.RuleDatabase

class RuleViewModelFactory(private val ruleDao: RuleDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RuleViewModel::class.java)) {
            RuleViewModel(ruleDao) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}