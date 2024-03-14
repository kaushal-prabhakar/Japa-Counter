package com.kaushal.japacounter.data.repository

import com.kaushal.japacounter.data.Entities
import com.kaushal.japacounter.data.JapaInfoRoomEntity
import com.kaushal.japacounter.data.Status
import com.kaushal.japacounter.data.db.RoomDB
import com.kaushal.japacounter.data.toEntities
import org.threeten.bp.Instant
import timber.log.Timber
import javax.inject.Inject

interface MainRepository {

    suspend fun addNewJapa(entities: Entities)

    suspend fun resetJapaCounter(name: String)

    suspend fun completeJapa(name: String)

    suspend fun deleteJapa(name: String)

    suspend fun getMyJapaList(): List<Entities>

    suspend fun getJapaDetails(japaName: String): Entities

    suspend fun updateJapaCounter(name: String, newCount: Int, currentCount: Int, target: Int?)
}

class MainRepositoryImpl @Inject constructor(private val roomDB: RoomDB) : MainRepository {

    override suspend fun addNewJapa(entities: Entities) {
        val japaInfoRoomEntity = JapaInfoRoomEntity(
            name = entities.name,
            goal = entities.goal,
            lastUpdatedTime = Instant.now(),
            incrementValue = entities.incrementValue,
            decrementValue = entities.decrementValue,
            currentValue = 0,
            status = Status.NOT_STARTED
        )

        val id = roomDB.dao().insertNewJapa(japaInfoRoomEntity)
        Timber.i("row id:$id")
    }

    override suspend fun resetJapaCounter(name: String) {
        roomDB.dao().resetJapaCounter(name, Instant.now(), Status.NOT_STARTED.name)
    }

    override suspend fun completeJapa(name: String) {
        roomDB.dao().completeJapa(name, Status.COMPLETED.name)
    }

    override suspend fun deleteJapa(name: String) {
        roomDB.dao().deleteJapa(name)
    }

    override suspend fun getMyJapaList(): List<Entities> {
        val roomEntityList =  roomDB.dao().getMyJapaList()
        return roomEntityList.map {
            it.toEntities()
        }
    }

    override suspend fun getJapaDetails(japaName: String): Entities {
        return roomDB.dao().getJapaDetails(japaName).toEntities()
    }

    override suspend fun updateJapaCounter(name: String, newCount: Int, currentCount: Int, target: Int?) {
        val differenceValue = newCount - currentCount
        if(newCount > currentCount) {
            roomDB.dao().incrementJapaCounter(name, newCount, differenceValue, Instant.now(), Status.ACTIVE.name)
        } else {
            roomDB.dao().decrementJapaCounter(name, newCount, differenceValue, Instant.now(), Status.ACTIVE.name)
        }
        if(target != null) {
            roomDB.dao().updateJapaTarget(target, name)
        }
    }

}