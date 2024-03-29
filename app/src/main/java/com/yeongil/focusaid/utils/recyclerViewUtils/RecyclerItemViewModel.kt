package com.yeongil.focusaid.utils.recyclerViewUtils

interface RecyclerItemViewModel {
    val id: String
    fun isSameItem(other: Any): Boolean
    fun isSameContent(other: Any): Boolean
    fun toRecyclerItem(): RecyclerItem
}