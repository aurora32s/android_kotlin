package com.haman.aop_part3_chapter06.home

data class ArticleModel(
    val seller : String,
    val title : String,
    val createdAt : Long,
    val price : String,
    val imageUrl : String
) {
    // firebase database 와 연동하기 위해서는 빈 생성자가 빌수
    constructor() : this("","",0,"","")
}
