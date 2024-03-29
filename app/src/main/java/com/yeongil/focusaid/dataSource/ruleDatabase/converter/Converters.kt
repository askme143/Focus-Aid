package com.yeongil.focusaid.dataSource.ruleDatabase.converter

import androidx.room.TypeConverter
import com.yeongil.focusaid.data.rule.action.AppBlockEntry
import com.yeongil.focusaid.data.rule.action.KeywordEntry
import com.yeongil.focusaid.data.rule.action.RingerAction.RingerMode
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromAppListToString(value: List<String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun fromStringToAppList(value: String): List<String> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromKeywordEntryListToString(value: List<KeywordEntry>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun fromStringToKeywordEntryList(value: String): List<KeywordEntry> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromAppBlockEntryListToString(value: List<AppBlockEntry>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun fromStringToAppBlockEntryList(value: String): List<AppBlockEntry> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromRepeatDayToString(value: List<Boolean>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun fromStringToRepeatDay(value: String): List<Boolean> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromRingerModeToInt(value: RingerMode): Int {
        return value.ordinal
    }

    @TypeConverter
    fun fromIntToRingerMode(value: Int): RingerMode {
        return enumValues<RingerMode>()[value]
    }
}