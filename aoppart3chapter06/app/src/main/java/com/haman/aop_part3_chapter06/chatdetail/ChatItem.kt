package com.haman.aop_part3_chapter06.chatdetail

data class ChatItem(
    val senderId : String,
    val message : String
) {
    constructor() : this ("","")
}
