package com.kaushal.japacounter.data.db

import androidx.room.TypeConverter
import com.kaushal.japacounter.ext.convertInstantToString
import com.kaushal.japacounter.ext.convertStringToInstant
import com.squareup.moshi.Moshi
import org.threeten.bp.Instant
import java.text.SimpleDateFormat
import java.util.Date

class TypeConverters {

    private val moshi = Moshi.Builder().build()

    companion object {
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm")
    }

    @TypeConverter
    fun fromInstant(instant: Instant?): String? {
        if(instant == null) return null
        return convertInstantToString(instant, moshi)
    }

    @TypeConverter
    fun toInstant(string: String?): Instant? {
        if (string.isNullOrEmpty()) return null
        return convertStringToInstant(string, moshi)
    }

    @TypeConverter
    fun fromDateToString(value: Date?): String? {
        if(value == null) return null
        return format.format(value)
    }
    @TypeConverter
    fun toDateFromString(value: String?) : Date? {
        if(value.isNullOrEmpty()) return null
        return format.parse(value)
    }

}