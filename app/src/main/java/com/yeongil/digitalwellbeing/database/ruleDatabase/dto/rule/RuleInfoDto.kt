package com.yeongil.digitalwellbeing.database.ruleDatabase.dto.rule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "rule_info")
data class RuleInfoDto(
    @PrimaryKey val rid: Int,
    @ColumnInfo(name = "rule_name") val ruleName: String,
    val activated: Boolean,
    @ColumnInfo(name = "noti_on_trigger") val notiOnTrigger: Boolean,
)