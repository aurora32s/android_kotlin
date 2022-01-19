package com.haman.aop_part2_chapter05

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val btnAddPhoto : Button by lazy {
        findViewById(R.id.btn_add_photo)
    }

    private val btnExecFrame : Button by lazy {
        findViewById(R.id.btn_exec_frame)
    }

    // 이미지 view 관리
    private val imageViewList : List<ImageView> by lazy {
        mutableListOf<ImageView>().apply {
            add(findViewById(R.id.img_first))
            add(findViewById(R.id.img_second))
            add(findViewById(R.id.img_third))
            add(findViewById(R.id.img_four))
            add(findViewById(R.id.img_five))
            add(findViewById(R.id.img_six))
            add(findViewById(R.id.img_seven))
            add(findViewById(R.id.img_eight))
            add(findViewById(R.id.img_nine))
        }
    }

    private val imageUriList : MutableList<Uri> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBtnAddPhoto() // 사진 추가하기 버튼 초기화
        initBtnExecFrame() // 전자액자 시작하기 버튼 초기화
    }

    private fun initBtnAddPhoto() {
        btnAddPhoto.setOnClickListener {
            // 1. 권한이 있는지 체크
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE // 외부 저장소 읽기 권한
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // READ_EXTERNAL_STORAGE 권한이 있는 경우
                    // TODO 갤러리에서 사진을 가지고 오는 기능 구현
                    navigatePhotos()
                }
                // 교육용 팝업을 띄울 필요가 있는 권한인지 확인
                shouldShowRequestPermissionRationale(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) -> {
                    // TODO 교육용 팝업 확인 후 권한 팝업 띄우는 기능 구현
                    showPermissionContextPopup()
                }
                else -> {
                    // 바로 권한 요청 팝업 생성
                    requestPermissions(
                        arrayOf(
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        1000
                    )
                }
            }
        }
    }

    /**
     * 전자액자 실행하기 버튼 클릭 메서드
     * 초기화해주는 메서드는 호출하는 위치와 가까이 작성해주는 것이 좋다.
     */
    private fun initBtnExecFrame() {
        // TODO 선택된 이미지 리스트를 가지고 PhotoFrameActivity 실행
        btnExecFrame.setOnClickListener {
            val intent = Intent(this, PhotoFrameActivity::class.java)
            imageUriList.forEachIndexed { index, uri ->
                // 각각의 이미지 URI 을 intent 에 저장
                intent.putExtra("photo$index", uri.toString())
            }
            intent.putExtra("photoListSize", imageUriList.size)
            startActivity(intent)
        }
    }

    // 권한 교육용 팝업 생성
    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("전자액자 앱에서 사진을 불러오기 위해 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    1000
                )
            }
            .setNegativeButton("취소하기") { dialog, which -> }
            .create()
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1000 -> { // 내가 권한을 요청할 때 사용한 requestCode
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO 권한이 부여가 되었을 때 사진을 가지고 오는 기능 구현
                    navigatePhotos()
                }
            }
            else -> {
                //
            }
        }
    }

    /**
     * 로컬 스토리지에서 사진을 가져오는 기능 구현
     */
    private fun navigatePhotos() {
        if (imageUriList.size >= 9) {
            Toast.makeText(this, "이미 사진이 꽉 찼습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        // Content Provider > Storage Access Framework(SAF)를 이용해서 사진 가져오기
        // SAF : 간결한 코드로 서비스 제공 -> intent 이용
        // SAF 를 사용하지 않고, 커스터마이징된 이미지 갤러리 화면을 구성할 수 있다.
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        // ACTION_GET_CONTENT : SAF 기능을 이용해 content 에 접근하는 intent 라는 의미
        intent.type = "image/*"
        startActivityForResult(intent, 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) {
            // 요청을 정상적으로 수행했을 경우에만 수행
            return
        }

        when (requestCode) {
            2000 -> {
                val imageUri : Uri? = data?.data // data 가 null 이면 뒤에 참조를 수행하지 않고 null 을 반환
                if (imageUri != null) {
                    imageUriList.add(imageUri)
                    imageViewList[imageUriList.size - 1].setImageURI(imageUri)
                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}