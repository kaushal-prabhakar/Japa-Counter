package com.kaushal.japacounter.utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
internal annotation class UTCDateTime

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
internal annotation class UTCOffsetDateTime


@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
internal annotation class TimeZoneId

//
//@Retention(AnnotationRetention.RUNTIME)
//@JsonQualifier
//internal annotation class ZoneOffsetDateTime

class UTCDateTimeAdapter {

    @ToJson
    fun toJson(@UTCDateTime value: Instant): String = value.toString()

    @FromJson
    @UTCDateTime
    fun fromJson(value: String): Instant = Instant.parse(value)

}

class UTCOffsetDateTimeAdapter {

    @ToJson
    fun toJson(@UTCOffsetDateTime value: OffsetDateTime): String = value.toString()

    @FromJson
    @UTCOffsetDateTime
    fun fromJson(value: String): OffsetDateTime = OffsetDateTime.parse(value)
}