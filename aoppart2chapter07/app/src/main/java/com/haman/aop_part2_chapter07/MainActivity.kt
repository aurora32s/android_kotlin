package com.haman.aop_part2_chapter07

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private val TAG = ".MainActivity"

    private val btnRecord : RecordButton by lazy {
        findViewById(R.id.btn_record)
    }

    private val btnReset : Button by lazy {
        findViewById(R.id.btn_reset)
    }

    private val visualizerView : SoundVisualizerView by lazy {
        findViewById(R.id.visualizerView)
    }

    private val txtTimer : CountUpView by lazy {
        findViewById(R.id.txt_record_time)
    }

    // 녹음 상태 저장
    private var state = State.BEFORE_RECORDING
        set(value) {
            field = value

            // TODO 녹음이 완료된 상태에서만 reset 가능하도록 수정
//            btnReset.isEnabled =
//                    value == State.AFTER_RECORDING || value == State.ON_PLAYING

            // 1. record button 이미지 업데이트
            btnRecord.updateIconWithState(value)
        }

    // 권한 요청 내역
    private val requiredPermission = arrayOf(
        android.Manifest.permission.RECORD_AUDIO
    )

    // 오디오 관리
    // 사용을 하지 않을 경우에는 release 하기 위해서 nullable 로 선언
    // 오디오와 비디오 같이 메모리를 많이 차지하는 콘텐츠는 항상 사용하지 않을 때는
    // 메모리에서 제거해주자!
    private var recorder : MediaRecorder? = null
    private var player : MediaPlayer? = null

    private val recordingFilePath : String by lazy {
        "${externalCacheDir?.absolutePath}/recording.3gp"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestAudioPermission()
        initView()
        bindViews()
        initVariables()
    }

    private fun initView () {
        btnRecord.updateIconWithState(state)
    }

    private fun requestAudioPermission () {
        requestPermissions(requiredPermission, REQUEST_RECORD_AUDIO_PERMISSION)
    }

    private fun bindViews () {
        // 녹음 버튼
        btnRecord.setOnClickListener {
            when (state) {
                State.BEFORE_RECORDING -> startRecording()
                State.ON_RECORDING -> stopRecording()
                State.AFTER_RECORDING -> startPlaying()
                State.ON_PLAYING -> stopPlaying()
            }
        }

        // 리셋 버튼
        btnReset.setOnClickListener {
            if (state == State.ON_RECORDING) {
                Toast.makeText(this, "현재 녹음중입니다...", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            stopPlaying()
            visualizerView.resetAmplitudes()
            txtTimer.clearCountTime()
            state = State.BEFORE_RECORDING
        }

        // 화면에 소리를 시각적으로 표시
        visualizerView.onRequestCurrentAmplitude = {
            recorder?.maxAmplitude ?: 0
        }
    }

    private fun initVariables () {
        state = State.BEFORE_RECORDING
    }

    private fun startRecording () {
        Log.d(TAG, "StartRecording")
        recorder = MediaRecorder()
            .apply {
                setAudioSource(MediaRecorder.AudioSource.MIC) // 미디어를 받아올 input
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                // save to app-specific storage documents 참고
                // 앱에서 접근할 수 있는 storage 의 범위가 한정되어 있다. (내부, 특정 외부 저장소[cache 등])
                setOutputFile(recordingFilePath)
                prepare()
            }
        recorder?.start()
        visualizerView.startVisualizing(false)
        txtTimer.startCountUp()
        state = State.ON_RECORDING
    }

    private fun stopRecording () {
        Log.d(TAG, "StopRecording")
        recorder?.run {
            stop()
            release()
        }
        // 메모리에서 해제시켜주는 것에 주의하자.
        recorder = null
        visualizerView.stopVisualizing()
        txtTimer.stopCountUp()
        state = State.AFTER_RECORDING
    }

    private fun startPlaying () {
        Log.d(TAG, "StartPlaying")
        // 재생은 MediaPlayer
        player = MediaPlayer()
            .apply {
                setDataSource(recordingFilePath)
                prepare()
                // prepareAsyn() : 파일을 받아오는데 상당한 시간이 걸릴 경우에는 비동기적으로 준비시키자.
            }

        player?.setOnCompletionListener {
            stopPlaying()
        }

        player?.start()
        visualizerView.startVisualizing(true)
        txtTimer.startCountUp()
        state = State.ON_PLAYING
    }

    private fun stopPlaying () {
        Log.d(TAG, "StopPlaying")
        player?.run {
            release()
        }
        // 메모리에서 해제시켜주는 것에 유의하라.
        player = null
        visualizerView.stopVisualizing()
        txtTimer.stopCountUp()
        state = State.AFTER_RECORDING
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val audioRecordPermissionGranted =
            requestCode == REQUEST_RECORD_AUDIO_PERMISSION &&
                    grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

        if (!audioRecordPermissionGranted) {
            finish()
        }
    }

    companion object {
        val REQUEST_RECORD_AUDIO_PERMISSION = 202
    }
}