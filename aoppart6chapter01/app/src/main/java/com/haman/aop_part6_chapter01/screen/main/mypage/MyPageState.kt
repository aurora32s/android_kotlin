package com.haman.aop_part6_chapter01.screen.main.mypage

import android.net.Uri
import androidx.annotation.StringRes
import com.haman.aop_part6_chapter01.data.entity.impl.OrderEntity
import com.haman.aop_part6_chapter01.model.order.OrderModel

sealed interface MyPageState{

    object UnInitialized: MyPageState

    object Loading: MyPageState

    data class Login(
        val idToken: String
    ): MyPageState

    sealed interface Success: MyPageState {

        data class Registered(
            val userName: String,
            val profileImageUri: Uri?,
            val orderList: List<OrderModel>
        ): Success

        object NotRegistered: Success

    }

    data class Error(
        @StringRes val messageId: Int,
        val e: Throwable
    ): MyPageState

}