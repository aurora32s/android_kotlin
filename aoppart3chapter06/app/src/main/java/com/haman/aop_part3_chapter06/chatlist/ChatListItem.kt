package com.haman.aop_part3_chapter06.chatlist

data class ChatListItem(
    val buyerId : String,
    val sellerId : String,
    val key : Long,
    val itemsTitle : String
) {
    constructor() : this("","",0L,"")
}
