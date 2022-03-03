package com.haman.aop_part5_chapter05.domain

data class ArrivalInformation(
    val subway: Subway,
    val direction: String,
    val message: String,
    val destination: String,
    val updatedAt: String
)
