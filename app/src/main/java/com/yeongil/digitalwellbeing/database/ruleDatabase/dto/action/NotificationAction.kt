package com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "notification_actions")
data class NotificationAction(
    @PrimaryKey val rid: Int,
    @ColumnInfo(name = "app_list") val appList: List<String>,
    @ColumnInfo(name = "keyword_list") val keywordList: List<KeywordEntryDto>,
    @ColumnInfo(name = "handling_action") val handlingAction: Int,
)