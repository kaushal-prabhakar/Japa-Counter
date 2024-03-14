package com.kaushal.japacounter.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaushal.japacounter.data.Entities
import com.kaushal.japacounter.data.JapaInfoRoomEntity
import org.threeten.bp.Instant

@Dao
interface Dao {

    @Query("SELECT * FROM Japa_Info")
    suspend fun getMyJapaList(): List<JapaInfoRoomEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewJapa(japaInfoRoomEntity: JapaInfoRoomEntity) : Long

    @Query("UPDATE Japa_Info SET currentValue = :value, incrementValue = :incrementValue, lastUpdated = :dateTime, status = :status WHERE japaName = :name")
    suspend fun incrementJapaCounter(name: String, value: Int, incrementValue: Int , dateTime: Instant, status: String)

    @Query("UPDATE Japa_Info SET currentValue = :value, decrementValue = :decrementValue, lastUpdated = :dateTime, status = :status WHERE japaName = :name")
    suspend fun decrementJapaCounter(name: String, value: Int, decrementValue: Int , dateTime: Instant, status: String)

    @Query("UPDATE japa_info SET currentValue = 0, incrementValue = 0, decrementValue = 0, lastUpdated = :dateTime, status = :status WHERE japaName = :name")
    suspend fun resetJapaCounter(name: String, dateTime: Instant, status: String)

    @Query("UPDATE japa_info SET status = :status WHERE japaName = :name")
    suspend fun completeJapa(name: String, status: String)

    @Query("DELETE FROM JAPA_INFO WHERE japaName = :name")
    suspend fun deleteJapa(name: String)

    @Query("SELECT currentValue FROM JAPA_INFO WHERE japaName = :name")
    suspend fun getCurrentValue(name: String): Int?

    @Query("UPDATE japa_info SET goal = :goal WHERE japaName = :name")
    suspend fun updateJapaTarget(goal: Int, name: String)

    @Query("SELECT * FROM JAPA_INFO WHERE japaName = :name")
    suspend fun getJapaDetails(name: String): JapaInfoRoomEntity
}