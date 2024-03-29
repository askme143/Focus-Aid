package com.yeongil.focusaid.viewModel.viewModel.rule

import androidx.lifecycle.*
import com.yeongil.focusaid.data.rule.RuleInfo
import com.yeongil.focusaid.repository.RuleRepository
import com.yeongil.focusaid.utils.Event
import com.yeongil.focusaid.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.focusaid.viewModel.itemViewModel.HelpPhraseItemViewModel
import com.yeongil.focusaid.viewModel.itemViewModel.RuleInfoItemViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RuleInfoViewModel(
    private val ruleRepo: RuleRepository,
) : ViewModel() {
    val ruleInfoItemList: LiveData<List<RecyclerItem>> = liveData {
        ruleRepo.getRuleInfoListFlow().collect {
            it.map { ruleInfo ->
                RuleInfoItemViewModel(
                    ruleInfo.ruleId.toString(),
                    ruleInfo,
                    onClickActivate,
                    onClickNotiOnTrigger,
                    onClickDelete,
                    onClickItem
                ).toRecyclerItem()
            }.also { recyclerItems -> emit(recyclerItems) }
        }
    }
    val recyclerItemList = ruleInfoItemList.map {
        if (it.isNotEmpty()) {
            val text = "현재 ${it.size}개의 방해 관리 \n규칙이 있습니다."
            it + HelpPhraseItemViewModel(text).toRecyclerItem()
        } else it
    }

    val itemClickEvent = MutableLiveData<Event<Int>>()
    val itemClickActivateEvent = MutableLiveData<Event<Pair<Int, Boolean>>>()
    val itemClickNotiOnTriggerEvent = MutableLiveData<Event<Pair<Int, Boolean>>>()
    val itemDeleteEvent = MutableLiveData<Event<Unit>>()
    val itemDeleteCompleteEvent = MutableLiveData<Event<Int>>()
    private val _deletingRule = MutableLiveData<Pair<Int, String>>()
    val deletingRuleName: LiveData<String> get() = _deletingRule.map { it.second }


    private val onClickActivate: (RuleInfo) -> Unit = {
        val newRuleInfo = it.copy(activated = !it.activated)
        itemClickActivateEvent.value = Event(Pair(it.ruleId, newRuleInfo.activated))
        viewModelScope.launch {
            ruleRepo.updateRuleInfo(newRuleInfo)
        }
    }
    private val onClickNotiOnTrigger: (RuleInfo) -> Unit = {
        val newRuleInfo = it.copy(notiOnTrigger = !it.notiOnTrigger)
        itemClickNotiOnTriggerEvent.value = Event(Pair(it.ruleId, newRuleInfo.notiOnTrigger))
        viewModelScope.launch { ruleRepo.updateRuleInfo(newRuleInfo) }
    }
    private val onClickDelete: (ruleId: Int, ruleName: String) -> Unit = { ruleId, ruleName ->
        itemDeleteEvent.value = Event(Unit)
        _deletingRule.value = Pair(ruleId, ruleName)
    }
    private val onClickItem: (Int) -> Unit = { ruleId ->
        itemClickEvent.value = Event(ruleId)
    }

    fun deleteRule() {
        viewModelScope.launch {
            val rid = _deletingRule.value!!.first

            ruleRepo.deleteRuleByRid(rid)
            withContext(Dispatchers.Main) {
                itemDeleteCompleteEvent.value = Event(rid)
            }
        }
    }
}