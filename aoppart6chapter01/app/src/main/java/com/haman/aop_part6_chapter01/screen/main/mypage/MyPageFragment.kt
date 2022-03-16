package com.haman.aop_part6_chapter01.screen.main.mypage

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.haman.aop_part6_chapter01.BuildConfig
import com.haman.aop_part6_chapter01.R
import com.haman.aop_part6_chapter01.databinding.FragmentMypageBinding
import com.haman.aop_part6_chapter01.extension.load
import com.haman.aop_part6_chapter01.screen.base.BaseFragment
import com.haman.aop_part6_chapter01.screen.main.home.HomeFragment
import com.haman.aop_part6_chapter01.widget.adapter.ModelRecyclerAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class MyPageFragment : BaseFragment<MyPageViewModel, FragmentMypageBinding>() {
    override val viewModel: MyPageViewModel by viewModel()

    override fun getViewBinding(): FragmentMypageBinding =
        FragmentMypageBinding.inflate(layoutInflater)

    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    private val gsc by lazy { GoogleSignIn.getClient(requireActivity(), gso) }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val loginLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            Log.d(",MyPageFragment", result.toString())
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    task.getResult(ApiException::class.java)?.let { account ->
                        viewModel.saveToken(account.idToken ?: throw java.lang.Exception())
                    } ?: kotlin.run {
                        throw Exception()
                    }
                } catch(exception: Exception){
                    exception.toString()
                    binding.progressbar.isGone = true
                }
            }
        }

    private val adapter by lazy {
        ModelRecyclerAdapter<>
    }

    override fun initViews() = with(binding) {
        super.initViews()
        loginButton.setOnClickListener {
            signInGoogle()
        }
        logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            viewModel.signOut()
        }
    }

    private fun signInGoogle() {
        val signInIntent = gsc.signInIntent
        loginLauncher.launch(signInIntent)
    }

    override fun observeData() = viewModel.myStateLiveData.observe(viewLifecycleOwner) {
        when(it) {
            is MyPageState.Error -> handleErrorState(it)
            MyPageState.Loading -> handleLoadingState()
            is MyPageState.Login -> handleLoginState(it)
            is MyPageState.Success -> handleSuccessState(it)
            MyPageState.UnInitialized -> Unit
        }
    }

    private fun handleLoadingState() {
        binding.loginRequiredGroup.isGone = true
        binding.progressbar.isVisible = true
    }

    private fun handleSuccessState(state: MyPageState.Success) = with(binding) {
        progressbar.isGone = true

        when (state) {
            is MyPageState.Success.Registered -> handleRegisteredState(state)
            MyPageState.Success.NotRegistered -> {
                profileGroup.isGone = true
                loginRequiredGroup.isVisible = true
            }
        }
    }

    private fun handleRegisteredState(state: MyPageState.Success.Registered) = with(binding) {
        profileGroup.isVisible = true
        loginRequiredGroup.isGone = true

        profileImageView.load(state.profileImageUri.toString(), 60f)
        userNameTextView.text = state.userName
    }

    private fun handleLoginState(state: MyPageState.Login) {
        binding.progressbar.isVisible = true
        val credential = GoogleAuthProvider.getCredential(state.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    viewModel.setUserInfo(user)
                } else {
                    firebaseAuth.signOut()
                }
            }
    }

    private fun handleErrorState(state: MyPageState.Error) {
        Toast.makeText(requireContext(), getString(state.messageId), Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "MyPageFragment"
        fun newInstance() = MyPageFragment()
    }
}