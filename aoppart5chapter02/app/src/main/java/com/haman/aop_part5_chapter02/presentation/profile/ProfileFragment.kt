package com.haman.aop_part5_chapter02.presentation.profile

import android.app.Activity
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.haman.aop_part5_chapter02.R
import com.haman.aop_part5_chapter02.databinding.FragmentProfileBinding
import com.haman.aop_part5_chapter02.extensions.loadCenterCrop
import com.haman.aop_part5_chapter02.extensions.toast
import com.haman.aop_part5_chapter02.presentation.BaseFragment
import com.haman.aop_part5_chapter02.presentation.adapter.ProductListAdapter
import com.haman.aop_part5_chapter02.presentation.detail.ProductDetailActivity
import org.koin.android.ext.android.inject

internal class ProfileFragment: BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    companion object {
        const val TAG = "ProfileFragment"
    }

    private val adapter = ProductListAdapter()
    override val viewModel: ProfileViewModel by inject()
    override fun getViewBinding(): FragmentProfileBinding =
        FragmentProfileBinding.inflate(layoutInflater)

    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    private val gsc by lazy {
        GoogleSignIn.getClient(requireActivity(), gso)
    }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val loginLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    task.getResult(ApiException::class.java)?.let { account ->
                        // TODO saveToken
                        viewModel.saveToken(account.idToken ?: throw Exception())
                    } ?: throw Exception()
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
        }

    override fun observeData() = viewModel.profileStateLiveData.observe(this) {
        when (it) {
            ProfileState.Uninitialized -> { initViews() }
            ProfileState.Loading -> { handleLoadingState() }
            is ProfileState.Login -> { handleLoginState(it) }
            is ProfileState.Success.NotRegistered -> { handleNotRegisteredState(it) }
            is ProfileState.Success.Registered -> { handleRegisteredState(it) }
            ProfileState.Error -> { handleErrorState() }
        }
    }

    private fun initViews() = with(binding) {
        recyclerView.adapter = adapter

        loginButton.setOnClickListener {
            signInGoogle()
        }
        logoutButton.setOnClickListener {
            viewModel.signOut()
        }
    }

    private fun handleLoadingState() = with(binding) {
        progressbar.isVisible = true
        profileGroup.isVisible = false
        loginRequiredGroup.isVisible = false
    }

    private fun handleErrorState() {
        requireContext().toast("에러가 발생하였습니다.")
    }

    private fun signInGoogle() {
        val signInIntent = gsc.signInIntent
        loginLauncher.launch(signInIntent)
    }

    private fun handleLoginState(state: ProfileState.Login) = with(binding) {
        val credential = GoogleAuthProvider.getCredential(state.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    viewModel.setUserInfo(user)
                } else {
                    viewModel.setUserInfo()
                }
            }
    }

    private fun handleNotRegisteredState(state: ProfileState.Success) = with(binding) {
        progressbar.isVisible = false
        profileGroup.isVisible = false
        loginRequiredGroup.isVisible = true
    }

    private fun handleRegisteredState(state: ProfileState.Success.Registered) = with(binding) {
        progressbar.isVisible = false
        profileGroup.isVisible = true
        loginRequiredGroup.isVisible = false

        profileImageView.loadCenterCrop(state.profileImageUrl.toString(), 60f)
        userNameTextView.text = state.userName

        Log.d(TAG, state.productList.toString())
        if (state.productList.isEmpty()) {
            emptyResultTextView.isVisible = true
            recyclerView.isVisible = false
        } else {
            emptyResultTextView.isVisible = false
            recyclerView.isVisible = true
            adapter.setData(state.productList) {
                startActivity(
                    ProductDetailActivity.netIntent(requireContext(), it.id)
                )
            }
        }
    }
}