package com.haman.aop_part4_chapter03.model

data class SearchResultEntity(
    val fullAddress: String,
    val name: String,
    val locationLatLng: LocationLatLngEntity
)
