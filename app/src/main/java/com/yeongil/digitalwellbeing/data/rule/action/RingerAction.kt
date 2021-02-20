package com.yeongil.digitalwellbeing.data.rule.action

import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

@Serializable
data class RingerAction(
    @ColumnInfo(name = "ringer_mode") val ringerMode: Int,
)