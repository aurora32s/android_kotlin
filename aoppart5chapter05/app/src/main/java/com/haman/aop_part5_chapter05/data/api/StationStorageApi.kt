package com.haman.aop_part5_chapter05.data.api

import com.google.firebase.storage.FirebaseStorage
import com.haman.aop_part5_chapter05.data.db.entity.StationEntity
import com.haman.aop_part5_chapter05.data.db.entity.SubwayEntity
import kotlinx.coroutines.tasks.await

class StationStorageApi(
    firebaseStorage: FirebaseStorage
): StationApi {

    private val sheetReference = firebaseStorage.reference.child(STATION_DATA_FILE_NAME)

    /**
     * metadata: return task
     * coroutine-play-service dependency 에 의해 task 에 대해서 await 할 수 있다.
     */
    override suspend fun getStationDataUpdatedTimeMills(): Long =
        sheetReference.metadata.await().updatedTimeMillis

    override suspend fun getStationSubway(): List<Pair<StationEntity, SubwayEntity>> {
        val downloadSizeBytes = sheetReference.metadata.await().sizeBytes
        val byteArray = sheetReference.getBytes(downloadSizeBytes).await()

        /**
         * decodeToString: column1, column2
         */
        return byteArray.decodeToString()
            .lines()
            .drop(1) // header 영역 제거
            .map { it.split(",") }
            .map { StationEntity(it[1]) to SubwayEntity(it[0].toInt()) }
    }

    companion object {
        private const val STATION_DATA_FILE_NAME = "station_data.csv"
    }
}