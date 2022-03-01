package com.haman.aop_part3_chapter06.home

data class ArticleModel(
    val seller : String,
    val title : String,
    val createdAt : Long,
    val content : String,
    val imageUrlList : List<String>
) {
    // firebase database 와 연동하기 위해서는 빈 생성자가 빌수
    constructor() : this("","",0,"", listOf())
}
