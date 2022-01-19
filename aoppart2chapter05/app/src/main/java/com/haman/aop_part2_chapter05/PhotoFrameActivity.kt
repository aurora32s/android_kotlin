package com.haman.aop_part2_chapter05

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity : AppCompatActivity() {
    private val TAG = ".PhotoFrameActivity"

    private val photoList = mutableListOf<Uri>()

    private val imgForeground : ImageView by lazy {
        findViewById(R.id.img_foreground)
    }

    private val imgBackground : ImageView by lazy {
        findViewById(R.id.img_background)
    }

    private var currentImgPosition = 0

    private var timer : Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_frame)

        // intent 로 전달된 이미지 URI 저장
        getPhotoUriFromIntent()
        Log.d(TAG, "onCreate!!!")
    }

    private fun getPhotoUriFromIntent () {
        val size = intent.getIntExtra("photoListSize", 0)
        for ( i in 0..size) {
            intent.getStringExtra("photo$i")?.let {
                photoList.add(Uri.parse(it))
            }
        }
    }

    private fun startTimer () {
        timer = timer(period = 5000/*mm*/) {
            // 마지막 인자에 람다식을 넣는 경우, 괄호의 밖으로 뺄 수 있다.
            // timer 는 메인 스레드가 아닌 점에 유의하자!!
            runOnUiThread {
                val current = currentImgPosition
                val next = if (photoList.size - 1 <= current) 0 else current + 1

                imgBackground.setImageURI(photoList[current])
                imgForeground.alpha = 0f // 투명도(0:안 보임)
                imgForeground.setImageURI(photoList[next])
                imgForeground.animate()
                    .alpha(1.0f)
                    .setDuration(1000)
                    .start()

                currentImgPosition = next
                Log.d(TAG, "timer process..................")
            }
        }
        // timer : 실행중인 timer 가 있다면 앱을 종료하고 난 이후에도 background 에서 계속 실행된다.
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop!!! timer cancel!")
        timer?.cancel()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart!!! timer start!")
        // onCreate 가 아닌 onStart 에서 timer 시작
        startTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy!!! timer cancel!")
        timer?.cancel() // 한번더 확인 사살!
    }
}