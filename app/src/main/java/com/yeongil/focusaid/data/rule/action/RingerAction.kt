package com.yeongil.focusaid.data.rule.action

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class RingerAction(
    @ColumnInfo(name = "ringer_mode") val ringerMode: RingerMode,
) : Parcelable {
    @Parcelize
    enum class RingerMode : Parcelable { VIBRATE, RING, SILENT }
}