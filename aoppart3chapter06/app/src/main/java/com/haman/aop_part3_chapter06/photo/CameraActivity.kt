package com.haman.aop_part3_chapter06.photo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.display.DisplayManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.core.AspectRatio.RATIO_4_3
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
import androidx.camera.core.ImageCapture.FLASH_MODE_AUTO
import androidx.camera.core.impl.ImageOutputConfig
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.haman.aop_part3_chapter06.databinding.ActivityCameraBinding
import com.haman.aop_part5_chapter03.extenstions.loadCenterCrop
import com.haman.aop_part5_chapter03.util.PathUtil
import java.io.File
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCameraBinding.inflate(layoutInflater) }

    // camera setting
    private val cameraProviderFuture by lazy { ProcessCameraProvider.getInstance(this) }
    private val displayManager by lazy {
        getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    private var camera: Camera? = null
    private val cameraMainExecutor by lazy { ContextCompat.getMainExecutor(this) }
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var imageCapture: ImageCapture
    private var contentUri: Uri? = null
    private var root: View? = null

    private var displayId: Int = -1

    // flash
    private var isFlashEnabled: Boolean = false

    // display 가 변경되는 것을 감지 (Ex. 화면 방향 변경)
    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(p0: Int) {}
        override fun onDisplayRemoved(p0: Int) {}
        @SuppressLint("RestrictedApi")
        override fun onDisplayChanged(p0: Int) {
            if (this@CameraActivity.displayId == displayId) {
                // TODO 화면이 회전되었을 경우 처리
                if (::imageCapture.isInitialized && root != null) {
                    imageCapture.targetRotation =
                        root?.display?.rotation ?: ImageOutputConfig.INVALID_ROTATION
                }
            }
        }
    }

    // capture
    private var isCapturing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        root = binding.root

        if (allPermissionsGranted()) { // 모든 권한 ok
            startCamera(binding.viewFinder)
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private var uriList = mutableListOf<Uri>()

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        Log.d(".MainActivity", it)
        Log.d(".MainActivity", "${PackageManager.PERMISSION_GRANTED}")
        Log.d(".MainActivity", ContextCompat.checkSelfPermission(
            baseContext, it
        ).toString())
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    // camera start
    private fun startCamera(viewFinder: PreviewView) {
//        displayManager.registerDisplayListener(displayListener, null)
        // 새로운 thread 기반으로 카메라 실행
        cameraExecutor = Executors.newSingleThreadExecutor()
        viewFinder.postDelayed({
            displayId = viewFinder.display.displayId
            bindCameraUseCase()
        }, 10)
    }

    private fun bindCameraUseCase() = with(binding) {
        // 화면 회전
        val rotation = viewFinder.display.rotation
        // camera selector(전면 카메라, 후방 카메라)
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(LENS_FACING)
            .build() // 카메라 설정(후면 카메라)


        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preView = Preview.Builder().apply {
                setTargetAspectRatio(RATIO_4_3)
                setTargetRotation(rotation)
//                 크기: default 는 최고 해상도
//                setTargetResolution()
            }.build()
            // image capture
            val imageCaptureBuilder = ImageCapture.Builder()
                .setCaptureMode(CAPTURE_MODE_MINIMIZE_LATENCY) // 지연 최소화
                .setTargetAspectRatio(RATIO_4_3)
                .setTargetRotation(rotation)
                .setFlashMode(FLASH_MODE_AUTO)
            imageCapture = imageCaptureBuilder.build()

            try {
                // 카메라가 쓰이고 있는 경우, binding 해제
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    this@CameraActivity,
                    cameraSelector,
                    preView,
                    imageCapture
                )
                preView.setSurfaceProvider(viewFinder.surfaceProvider)
                bindCaptureListener()
                bindZoomListener()
                initFlashAndAddListener()
                bindPreviewImageViewClickListener()
            } catch (exception: Exception) {
                exception.printStackTrace()
                Log.d(".MainActivity", exception.toString())
            }
        }, cameraMainExecutor)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun bindZoomListener() = with(binding) {
        // preview 내에서 터치 이벤트 리스너
        val listener = object: ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val currentZoomRatio = camera?.cameraInfo?.zoomState?.value?.zoomRatio ?: 1f
                val delta = detector.scaleFactor
                camera?.cameraControl?.setZoomRatio(currentZoomRatio * delta)
                return true
            }
        }

        val scaleGestureDetector = ScaleGestureDetector(this@CameraActivity, listener)
        viewFinder.setOnTouchListener {_, event ->
            scaleGestureDetector.onTouchEvent(event)
            return@setOnTouchListener true
        }
    }

    private fun initFlashAndAddListener() = with(binding) {
        val hasFlash = camera?.cameraInfo?.hasFlashUnit() ?: false
        flashSwitch.isVisible = hasFlash.not()
        if (hasFlash) { // flash 사용 가능
            flashSwitch.setOnCheckedChangeListener { _, isChecked ->
                isFlashEnabled = isChecked
            }
        } else { // 방어 코드 추가
            isFlashEnabled = false
            flashSwitch.setOnCheckedChangeListener(null)
        }
    }

    private fun bindCaptureListener() = with(binding) {
        captureButton.setOnClickListener {
            Log.d(".MainActivity", "capturing >>> $isCapturing")
            if (!isCapturing) {
                isCapturing = true
                captureCamera()
            }
        }
    }

    private fun updateSavedImageContent() {
        contentUri?.let {
            isCapturing = try {
                val file = File(
                    PathUtil.getPath(this, it) ?: throw FileNotFoundException()
                )
                MediaScannerConnection.scanFile(
                    this,
                    arrayOf(file.path),
                    arrayOf("image/jpeg"),
                    null
                )
                Handler(Looper.getMainLooper()).post {
                    binding.previewImageView.loadCenterCrop(url = it.toString(), corner = 4f)
                }
                uriList.add(it)
                flashLight(false)
                false
            } catch (exception: Exception) {
                Toast.makeText(this, "파일이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
                exception.printStackTrace()
                flashLight(false)
                false
            }
        }
    }

    // camera 저장
    private fun captureCamera() {
        if (::imageCapture.isInitialized.not()) return
        // 저장할 파일 생성
        val photoFile = File(
            PathUtil.getOutputDirectory(this),
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.KOREA
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        val name = SimpleDateFormat(
                FILENAME_FORMAT, Locale.KOREA
            ).format(System.currentTimeMillis())
        val contendValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }
        // output stream
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contendValues
            ).build()

        if (isFlashEnabled) { // flash on
            flashLight(true)
        }

        imageCapture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                    contentUri = savedUri
                    updateSavedImageContent()
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                    isCapturing = false
                    flashLight(false)
                }
            })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val permissionGranted = requestCode == REQUEST_CODE_PERMISSIONS &&
                allPermissionsGranted()

        if (permissionGranted) {
            startCamera(binding.viewFinder)
        } else {
            Toast.makeText(this, "카메라 권한이 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun flashLight(light: Boolean) {
        val hasFlash = camera?.cameraInfo?.hasFlashUnit() ?: false // 방어 코드

        if  (hasFlash) {
            camera?.cameraControl?.enableTorch(light)
        }
    }

    private fun bindPreviewImageViewClickListener() = with(binding) {
        previewImageView.setOnClickListener {
            startActivityForResult(
                ImagePreviewListActivity.newIntent(this@CameraActivity, uriList),
                CONFIRM_IMAGE_REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONFIRM_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    companion object {
        // permission
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )

        private val LENS_FACING: Int = CameraSelector.LENS_FACING_BACK // 후면 카메라

        // capture
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

        private const val CONFIRM_IMAGE_REQUEST_CODE = 3000
        private const val URI_LIST_KEY = "uriList"

        fun newIntent(activity: Activity) = Intent(activity, CameraActivity::class.java)
    }
}