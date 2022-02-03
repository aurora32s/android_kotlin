package com.haman.aop_part3_chapter06.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.haman.aop_part3_chapter06.DBKey
import com.haman.aop_part3_chapter06.R

class AddArticleActivity : AppCompatActivity() {

    private var selectedImageUri : Uri? = null
    private val auth : FirebaseAuth by lazy { Firebase.auth }
    private val storage : FirebaseStorage by lazy { Firebase.storage }
    private val articleDB : DatabaseReference by lazy {
        Firebase.database.reference.child(DBKey.DB_ARTICLES)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_article)

        findViewById<Button>(R.id.imageAddButton).setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    startContentProvider()
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

        findViewById<Button>(R.id.submitButton).setOnClickListener {
            val title = findViewById<EditText>(R.id.titleEditText).text.toString()
            val price = findViewById<EditText>(R.id.priceEditText).text.toString()
            val sellerId = auth.currentUser?.uid.orEmpty()

//            if (sellerId.isNullOrBlank()) {
//                Toast.makeText(this, "로그인 해주세요.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }

            showProgress()

            // 이미지가 있다면 업로드 과정 추가
            if (selectedImageUri != null) {
                // 다른 thread 에서 값을 변경할 수 있는 가능성 방지
                val photoUri = selectedImageUri ?: return@setOnClickListener
                uploadPhoto(photoUri,
                    successHandler = { uri ->
                        uploadArticle(sellerId,title,"${price}원",uri)
                    },
                    errorHandler = {
                        Toast.makeText(this@AddArticleActivity, "사진 업로드에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        hideProgress()
                    }
                )
            } else {
                uploadArticle(sellerId,title,"${price}원","")
            }
        }
    }

    private fun showPermissionContextPopup () {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("사진을 가져오기 위해 필요합니다.")
            .setPositiveButton("동의"){_,_ ->
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
            .create()
            .show()
    }

    private fun startContentProvider () {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, CONTENT_REQUEST_CODE)
    }

    private fun uploadPhoto (
        uri : Uri,
        successHandler : (String) -> Unit,
        errorHandler : () -> Unit
    ) {
        val fileName = "${System.currentTimeMillis()}.png"
        storage.reference.child("article/photo").child(fileName)
            .putFile(uri)
            .addOnCompleteListener{
                if (it.isSuccessful) {
                    // 업로드 완료
                    storage.reference.child("article/photo").child(fileName)
                        .downloadUrl
                        .addOnSuccessListener { uri ->
                            successHandler(uri.toString())
                        }
                        .addOnFailureListener {
                            errorHandler()
                        }
                } else {
                    errorHandler()
                }
            }
    }

    private fun uploadArticle (
        sellerId : String, title : String, price : String, imageUrl : String
    ) {
        val model = ArticleModel(
            sellerId,
            title,
            System.currentTimeMillis(),
            price,
            imageUrl
        )
        articleDB.push().setValue(model)
        Toast.makeText(this, "등록되었습니다.", Toast.LENGTH_SHORT).show()
        hideProgress()
        finish()
    }

    private fun showProgress () {
        findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
    }

    private fun hideProgress () {
        findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
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
                    startContentProvider()
                } else {
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
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
            CONTENT_REQUEST_CODE -> {
                val uri = data?.data
                if (uri != null) {
                    Glide.with(this)
                        .load(uri)
                        .into(findViewById(R.id.photoImageView))
                    selectedImageUri = uri
                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1000
        private const val CONTENT_REQUEST_CODE = 2000
    }
}