package com.haman.aop_part3_chapter06.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.haman.aop_part3_chapter06.DBKey
import com.haman.aop_part3_chapter06.databinding.ActivityAddArticleBinding
import com.haman.aop_part3_chapter06.gallery.GalleryActivity
import com.haman.aop_part3_chapter06.photo.CameraActivity
import com.haman.aop_part3_chapter06.photo.adapter.PhotoListAdapter
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class AddArticleActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAddArticleBinding.inflate(layoutInflater) }
    private var imageUriList: ArrayList<Uri> = arrayListOf()
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val storage: FirebaseStorage by lazy { Firebase.storage }
    private val articleDB: DatabaseReference by lazy {
        Firebase.database.reference.child(DBKey.DB_ARTICLES)
    }

    private val adapter: PhotoListAdapter = PhotoListAdapter { uri ->
        removePhoto(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding) {
        recyclerView.adapter = adapter

        binding.imageAddButton.setOnClickListener {
            showPictureUploadDialog()
        }

        submitButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()
            val sellerId = auth.currentUser?.uid.orEmpty()

            if (sellerId.isNullOrBlank()) {
                Toast.makeText(this@AddArticleActivity, "????????? ????????????.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            showProgress()

            // ???????????? ????????? ????????? ?????? ??????
            if (imageUriList.isNotEmpty()) {
                // ?????? thread ?????? ?????? ????????? ??? ?????? ????????? ??????
                lifecycleScope.launch {
                    val results = uploadPhoto(imageUriList)
                    afterUploadPhoto(results, title, content, sellerId)
                }
            } else {
                uploadArticle(sellerId, title, content, listOf())
            }
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("????????? ???????????????.")
            .setMessage("????????? ???????????? ?????? ???????????????.")
            .setPositiveButton("??????") { _, _ ->
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
            .create()
            .show()
    }

    private fun startGalleryScreen() {
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "image/*"
//        startActivityForResult(intent, GALLERY_REQUEST_CODE)
        startActivityForResult(
            GalleryActivity.newIntent(this),
            GALLERY_REQUEST_CODE
        )
    }

    private fun startCameraScreen() {
        startActivityForResult(
            CameraActivity.newIntent(this),
            CAMERA_REQUEST_CODE
        )
    }

    private suspend fun uploadPhoto(uris: List<Uri>) = withContext(Dispatchers.IO) {
        val uploadDeferred: List<Deferred<Any>> = uris.mapIndexed { index, uri ->
            lifecycleScope.async {
                try {
                    val fileName = "image_${System.currentTimeMillis()}_$index.png"
                    return@async storage
                        .reference
                        .child("article/photo")
                        .child(fileName)
                        .putFile(uri)
                        .await()
                        .storage
                        .downloadUrl
                        .await()
                        .toString()
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    return@async Pair(uri, exception)
                }
            }
        }
        return@withContext uploadDeferred.awaitAll()
    }

    private fun afterUploadPhoto(
        results: List<Any>,
        title: String,
        content: String,
        userId: String
    ) {
        // error ????????? case handle
        val errorResults = results.filterIsInstance<Pair<Uri, Exception>>()
        val successResult = results.filterIsInstance<String>()

        when {
            errorResults.isNotEmpty() && successResult.isNotEmpty() -> {
                // ?????? ???????????? ????????? ??????
                photoUploadErrorButContinueDialog(
                    errorResults,successResult,title,content,userId
                )
            }
            errorResults.isNotEmpty() && successResult.isEmpty() -> {
                // ????????? ????????? ??????
                uploadError()
            }
            else -> {
                // ?????? ????????? ????????? ??????
                uploadArticle(userId, title, content, results.filterIsInstance<String>())
            }
        }
    }

    private fun uploadArticle(
        sellerId: String, title: String, content: String, imageUrlList: List<String>
    ) {
        val model = ArticleModel(
            sellerId,
            title,
            System.currentTimeMillis(),
            content,
            imageUrlList
        )
        articleDB.push().setValue(model)
        Toast.makeText(this, "?????????????????????.", Toast.LENGTH_SHORT).show()
        hideProgress()
        finish()
    }

    private fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showPictureUploadDialog()
                } else {
                    Toast.makeText(this, "????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) {
            return
        }

        when (requestCode) {
            GALLERY_REQUEST_CODE -> { // ??????????????? ?????? ??????
//                val uri = data?.data
//                if (uri != null) {
//                    imageUriList.add(uri)
//                    // TODO RecyclerView Adapter
//                    adapter.setPhotoList(imageUriList)
//                } else {
//                    Toast.makeText(this, "????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show()
//                }
                data?.let { intent ->
                    val urlList = intent.getParcelableArrayListExtra<Uri>(URI_LIST_KEY)
                    urlList?.let { list ->
                        imageUriList.addAll(list)
                        adapter.setPhotoList(imageUriList)
                    }
                } ?: kotlin.run {
                    Toast.makeText(this, "????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show()
                }
            }
            CAMERA_REQUEST_CODE -> { // ???????????? ?????? ??????
                data?.let { intent ->
                    val uriList = intent.getParcelableArrayListExtra<Uri>(URI_LIST_KEY)
                    uriList?.let { list ->
                        imageUriList.addAll(list)
                        // TODO RecyclerView Adapter
                        adapter.setPhotoList(imageUriList)
                    }
                } ?: kotlin.run {
                    Toast.makeText(this, "????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPictureUploadDialog() {
        AlertDialog.Builder(this)
            .setTitle("????????????")
            .setMessage("??????????????? ????????? ??????????????????.")
            .setPositiveButton("?????????") { _, _ ->
                checkExternalStoragePermission {
                    startCameraScreen()
                }
            }
            .setNegativeButton("?????????") { _, _ ->
                checkExternalStoragePermission {
                    startGalleryScreen()
                }
            }
            .create()
            .show()
    }

    private fun checkExternalStoragePermission(uploadAction: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                uploadAction()
            }
            shouldShowRequestPermissionRationale(
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                showPermissionContextPopup()
            }
            else -> {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun photoUploadErrorButContinueDialog(
        errorResults: List<Pair<Uri, Exception>>,
        successResults: List<String>,
        title: String,
        content: String,
        sellerId: String
    ) {
        AlertDialog.Builder(this)
            .setTitle("?????? ????????? ????????? ??????")
            .setMessage("???????????? ????????? ???????????? ????????????." + errorResults.map { (uri, _) ->
                "$uri\n"
            } + "???????????? ?????????????????????????")
            .setPositiveButton("?????????") {_,_ ->
                uploadArticle(sellerId,title,content,successResults)
            }
            .create()
            .show()
    }

    private fun uploadError() {
        Snackbar.make(binding.root, "????????? ???????????? ?????????????????????.", Snackbar.LENGTH_SHORT).show()
        binding.progressBar.isVisible = false
    }

    private fun removePhoto(uri: Uri) {
        imageUriList.remove(uri)
        adapter.setPhotoList(imageUriList)
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1000
        private const val GALLERY_REQUEST_CODE = 2000
        private const val CAMERA_REQUEST_CODE = 1002

        private const val URI_LIST_KEY = "uriList"
    }
}