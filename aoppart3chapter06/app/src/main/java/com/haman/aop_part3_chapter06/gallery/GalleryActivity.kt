package com.haman.aop_part3_chapter06.gallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.haman.aop_part3_chapter06.AopPart5Chapter04Application.Companion.appContext
import com.haman.aop_part3_chapter06.R
import com.haman.aop_part3_chapter06.databinding.ActivityGalleryBinding
import com.haman.aop_part3_chapter06.photo.adapter.PhotoListAdapter

class GalleryActivity : AppCompatActivity() {

    companion object {
        fun newIntent(activity: Activity) = Intent(activity, GalleryActivity::class.java)

        private  const val URI_LIST_KEY = "uriList"
    }

    private val viewModel by viewModels<GalleryViewModel>()
    private val binding by lazy { ActivityGalleryBinding.inflate(layoutInflater) }

    private val adapter = GalleryPhotoListAdapter{
        viewModel.selectPhoto(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observeState()
    }

    private fun observeState() = viewModel.galleryStateLiveDouble.observe(this) {
        when (it) {
            GalleryState.Unintialized -> initViews()
            GalleryState.Loading -> handleLoading()
            is GalleryState.Success -> handleSuccess(it)
            is GalleryState.Confirm -> handleConfirm(it)
        }
    }

    private fun initViews() = with(binding) {
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(GridDividerDecoration(this@GalleryActivity, R.drawable.bg_frame_gallery))
        confirmButton.setOnClickListener {
            viewModel.confirmCheckedPhoto()
        }

        viewModel.fetchData()
    }

    private fun handleLoading() = with(binding) {
        progressBar.isVisible = true
    }

    private fun handleSuccess(state: GalleryState.Success) = with(binding) {
        progressBar.isVisible = false
        recyclerView.isVisible = true
        adapter.setPhotoList(state.photoList)
    }

    private fun handleConfirm(state: GalleryState.Confirm) {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(URI_LIST_KEY, ArrayList(state.photoList.map { it.uri }))
        })
        finish()
    }
}