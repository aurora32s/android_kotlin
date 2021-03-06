package com.haman.aop_part6_chapter01.screen.review.gallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.haman.aop_part6_chapter01.R
import com.haman.aop_part6_chapter01.databinding.ActivityGalleryBinding
import com.haman.aop_part6_chapter01.screen.base.BaseActivity
import com.haman.aop_part6_chapter01.screen.review.AddReviewActivity
import com.haman.aop_part6_chapter01.util.decorate.GridDividerDecoration
import com.haman.aop_part6_chapter01.widget.adapter.impl.GalleryPhotoListAdapter
import com.haman.aop_part6_chapter01.widget.adapter.impl.PhotoListAdapter
import org.koin.android.ext.android.inject

class GalleryActivity : BaseActivity<GalleryViewModel, ActivityGalleryBinding>() {

    override val viewModel: GalleryViewModel by inject()

    override fun getViewBinding(): ActivityGalleryBinding =
        ActivityGalleryBinding.inflate(layoutInflater)

    private val photoAdapter by lazy {
        GalleryPhotoListAdapter{ photoModel ->
            viewModel.selectPhoto(photoModel)
        }
    }

    override fun initViews() = with(binding) {
        recyclerView.adapter = photoAdapter
        recyclerView.addItemDecoration(
            GridDividerDecoration(this@GalleryActivity, R.drawable.bg_frame_gallery)
        )
        confirmButton.setOnClickListener {
            viewModel.confirmCheckedPhoto()
        }
    }

    override fun observeData() = viewModel.galleryStateLiveData.observe(this) {
        when (it) {
            GalleryState.Unintialized -> Unit
            GalleryState.Loading -> handleLoadingState()
            is GalleryState.Success -> handleSuccessState(it)
            is GalleryState.Confirm -> handleConfirmState(it)
            GalleryState.Error -> handleErrorState()
        }
    }

    private fun handleLoadingState() = with(binding) {
        progressBar.isVisible = true
        recyclerView.isGone = true
    }

    private fun handleSuccessState(state: GalleryState.Success) = with(binding) {
        progressBar.isGone = true
        recyclerView.isVisible = true
        photoAdapter.setPhotoList(state.photoList)
    }

    private fun handleConfirmState(state: GalleryState.Confirm) {
        setResult(
            Activity.RESULT_OK,
            Intent().apply {
                putExtra(URI_LIST_KEY, ArrayList(state.photoList.map { it.uri }))
            }
        )
        finish()
    }

    private fun handleErrorState() {
        binding.progressBar.isGone = true
        Toast.makeText(
            this,
            "??????????????? ???????????? ???????????? ?????? ????????? ?????????????????????.",
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        // ??????????????? ?????? ??????
        const val URI_LIST_KEY = "uriList"

        fun newInstance(context: Context) = Intent(context, GalleryActivity::class.java)
    }
}