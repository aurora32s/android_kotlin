package com.haman.aop_part5_chapter02.presentation.profile

import android.net.Uri
import com.haman.aop_part5_chapter02.data.entity.product.ProductEntity

sealed class ProfileState {

    object Uninitialized: ProfileState()

    object Loading: ProfileState()

    data class Login(
        val idToken: String
    ): ProfileState()

    sealed class Success: ProfileState() {
        data class Registered(
            val userName: String,
            val profileImageUrl: Uri?,
            val productList: List<ProductEntity> = listOf()
        ): Success()

        object NotRegistered: Success()
    }

    object Error: ProfileState()
}