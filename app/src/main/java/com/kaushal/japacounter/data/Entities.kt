package com.kaushal.japacounter.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kaushal.japacounter.ext.ofPattern
import org.threeten.bp.Instant


enum class Status {
    COMPLETED, ACTIVE, NOT_STARTED
}

enum class UpdateCounterType {
    INCREMENT, DECREMENT, RESET
}

data class Entities(
    val name: String,
    val goal: Int?,
    val incrementValue: Int,
    val decrementValue: Int,
    val currentValue: Int,
    val lastUpdatedTime: String?,
    val status: Status
)

@Entity(tableName = "japa_info")
class JapaInfoRoomEntity(
    @PrimaryKey(autoGenerate = true) var rowId: Int = 0,
    @ColumnInfo(name = "japaName") val name: String,
    @ColumnInfo(name = "goal") val goal: Int?,
    @ColumnInfo(name = "lastUpdated") val lastUpdatedTime: Instant,
    @ColumnInfo(name = "incrementValue") val incrementValue: Int?,
    @ColumnInfo(name = "decrementValue") val decrementValue: Int?,
    @ColumnInfo(name = "currentValue") val currentValue: Int,
    @ColumnInfo(name = "status") val status: Status
)

fun JapaInfoRoomEntity.toEntities(): Entities {
    return Entities(
        name = this.name,
        goal = this.goal,
        incrementValue = this.incrementValue ?: 0,
        decrementValue = this.decrementValue ?: 0,
        currentValue = this.currentValue,
        lastUpdatedTime = this.lastUpdatedTime.ofPattern("dd MMM yyyy HH:mm"),
        status = this.status
    )
}

sealed class Outcome<out T> {

    data class Success<out T>(val data: T, val isEmptyList: Boolean): Outcome<T>()

    data class Failure(val e: Throwable, val msg : String): Outcome<Nothing>()

    data class Loading(val asOverlay: Boolean): Outcome<Nothing>()

    object Empty: Outcome<Nothing>()

    companion object {

        fun loading(asOverlay: Boolean): Outcome<Nothing> = Loading(asOverlay)

        fun <T> success(data: T, isEmptyList: Boolean = false): Outcome<T> = Success(data, isEmptyList)

        fun failure(e: Throwable, msg: String): Outcome<Nothing> = Failure(e, msg)
    }

}
