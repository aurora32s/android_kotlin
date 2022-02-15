package com.haman.aop_part4_chapter02.service

data class MusicDto(
    // entity : 서버로부터 받은 데이터 전체
    // model 과는 별도로 관리
    val musics: List<MusicEntity>
)
