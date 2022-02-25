package com.haman.aop_part5_chapter02.presentation.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.haman.aop_part5_chapter02.data.preference.PreferenceManager
import com.haman.aop_part5_chapter02.domain.DeleteOrderedProductListUseCase
import com.haman.aop_part5_chapter02.domain.GetOrderedProductListUseCase
import com.haman.aop_part5_chapter02.presentation.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class ProfileViewModel(
    private val preferenceManager: PreferenceManager,
    private val getOrderedProductListUseCase: GetOrderedProductListUseCase,
    private val deleteOrderedProductListUseCase: DeleteOrderedProductListUseCase
): BaseViewModel() {
    override fun fetchData(): Job = viewModelScope.launch{
        setState(ProfileState.Loading)
        preferenceManager.getIdToken()?.let {
            setState(
                ProfileState.Login(it)
            )
        } ?: kotlin.run {
            setState(
                ProfileState.Success.NotRegistered
            )
        }
    }

    private val _profileStateLiveData = MutableLiveData<ProfileState>(ProfileState.Uninitialized)
    val profileStateLiveData
        get() = _profileStateLiveData

    private fun setState(state: ProfileState) {
        _profileStateLiveData.postValue(state)
    }

    fun saveToken(idToken: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            preferenceManager.putIdToken(idToken)
            fetchData()
        }
    }

    fun setUserInfo(user: FirebaseUser? = null) = viewModelScope.launch {
        user?.let { user ->
            setState(
                ProfileState.Success.Registered(
                    user.displayName ?: "익명",
                    user.photoUrl,
                    getOrderedProductListUseCase()
                )
            )
        } ?: kotlin.run {
            setState(
                ProfileState.Success.NotRegistered
            )
        }
    }

    fun signOut() = viewModelScope.launch {
        setState(ProfileState.Loading)
        withContext(Dispatchers.IO) {
            preferenceManager.removeToken()
        }
        deleteOrderedProductListUseCase()
        fetchData()
        setState(
            ProfileState.Success.NotRegistered
        )
    }
}