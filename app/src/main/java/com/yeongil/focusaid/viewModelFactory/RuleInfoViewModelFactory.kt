package com.yeongil.focusaid.viewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeongil.focusaid.dataSource.SequenceNumber
import com.yeongil.focusaid.dataSource.focusAidApi.FocusAidApi
import com.yeongil.focusaid.dataSource.focusAidApi.FocusAidService
import com.yeongil.focusaid.dataSource.logDatabase.LogDatabase
import com.yeongil.focusaid.dataSource.ruleDatabase.RuleDatabase
import com.yeongil.focusaid.dataSource.user.UserInfoPref
import com.yeongil.focusaid.repository.LogRepository
import com.yeongil.focusaid.repository.RuleRepository
import com.yeongil.focusaid.viewModel.viewModel.rule.RuleInfoViewModel

@Suppress("UNCHECKED_CAST")
class RuleInfoViewModelFactory(private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RuleInfoViewModel::class.java)) {
            RuleInfoViewModel(
                RuleRepository(
                    SequenceNumber(context.applicationContext),
                    RuleDatabase.getInstance(context.applicationContext).ruleDao()
                ),
                LogRepository(
                    FocusAidApi.service,
                    UserInfoPref(context.applicationContext),
                    LogDatabase.getInstance(context.applicationContext).logDao()
                )
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}