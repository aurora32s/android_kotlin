package com.haman.aop_part5_chapter05.data.db.entity.mapper

import com.haman.aop_part5_chapter05.data.db.entity.StationEntity
import com.haman.aop_part5_chapter05.data.db.entity.StationWithSubwaysEntity
import com.haman.aop_part5_chapter05.data.db.entity.SubwayEntity
import com.haman.aop_part5_chapter05.domain.Station
import com.haman.aop_part5_chapter05.domain.Subway

fun StationWithSubwaysEntity.toStations() = Station(
    name = station.stationName,
    isFavorite = station.isFavorite,
    connectedSubways = subways.toSubways()
)

fun Station.toStationEntity() = StationEntity(
    stationName = name,
    isFavorite = isFavorite
)

fun List<SubwayEntity>.toSubways(): List<Subway> =
    map {
        Subway.findById(it.subwayId)
    }

fun List<StationWithSubwaysEntity>.toStations() =
    map {
        it.toStations()
    }