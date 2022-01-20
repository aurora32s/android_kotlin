package com.haman.aop_part2_chapter06

import android.annotation.SuppressLint
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    // 최신 방식은 View Binding
    private val txtMinute : TextView by lazy {
        findViewById(R.id.txt_minute)
    }

    private val txtSecond : TextView by lazy {
        findViewById(R.id.txt_second)
    }

    private val seekBarTime : SeekBar by lazy {
        findViewById(R.id.seekbar_time)
    }

    private var currentCountDownTimer : CountDownTimer? = null

    // Builder 패턴으로 생성해야 하는 SoundPool
    private val soundPool = SoundPool.Builder().build()
    private var tickingSoundId : Int? = null
    private var bellSoundId : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        initSounds()
    }

    override fun onPause() {
        super.onPause()
        // pause(streamId) : 특정 stream 만 일시정지
        soundPool.autoPause()
    }

    override fun onResume() {
        super.onResume()
        soundPool.autoResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 미디어 파일들은 메모리를 많이 차지하기 때문에
        // 앱을 사용하지 않는게 명확해 졌을 때
        // 메모리에서 해지해주어야 한다.
        soundPool.release()
    }

    // soundPool 초기화
    private fun initSounds() {
        tickingSoundId = soundPool.load(this, R.raw.timer_ticking, 1)
        bellSoundId = soundPool.load(this, R.raw.timer_bell, 1)
    }

    // event 와 로직 연결
    private fun bindViews() {
        seekBarTime.setOnSeekBarChangeListener(
            // object : 무명객체 생성
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean // 실제로 사용자가 변경한건지, 코드 상에서 변경한건지 알려주는 flag
                ) {
//                    txtMinute.text = "%02d".format(progress)
                    // updateSeekBarTimer 에서 값을 변경해도 해당 override 메서드가 호출되기 때문에
                    // 추가적인 조건문이 필요하다.
                    if (fromUser) {
                        updateRemainingTime(progress * 60 * 1000L)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    // TODO 기존에 실행되고 있는 countDownTimer 종료
                    stopCountDown()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    // TODO seekbar 조정이 끝나면 카운트 다운 시작
                    seekBar ?: return // a ?: b -> a가 null 인 경우, b를 실행

                    if (seekBar.progress == 0) {
                        stopCountDown()
                    } else {
                        startCountDown()
                    }
                }
            }
        )
    }

    private fun createCountDownTimer (initialMillis : Long) = // 함수 표현식
        object : CountDownTimer(initialMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateRemainingTime(millisUntilFinished)
                updateSeekBarTimer(millisUntilFinished)
            }

            override fun onFinish() {
                completeCountDown()
            }
        }

    private fun startCountDown () {
        currentCountDownTimer = createCountDownTimer(seekBarTime.progress * 60 * 1000L)
        currentCountDownTimer?.start()

        // ticking 소리 재생 시작
        // Volume : 1F 가 최대
        // loop : 0 - no loop, -1 - loop forever
        // tickingSoundId 가 null 이 아닌 경우 let 메서드 호출
        tickingSoundId?.let { soundId ->
            soundPool.play(soundId, 1F, 1F, 0, -1, 1F)
        }
    }

    private fun stopCountDown () {
        currentCountDownTimer?.cancel()
        currentCountDownTimer = null // 꼭 필요하지 않지만, 초기값을 동일하게 유지하기 위해 추가

        // 재생되고 있는 사운드도 종료
        soundPool.autoPause()
    }

    private fun completeCountDown () {
        updateRemainingTime(0)
        updateSeekBarTimer(0)

        // TODO 알림 소리 추가
        soundPool.autoPause() // ticking 소리 종료
        bellSoundId?.let { soundId ->
            soundPool.play(soundId, 1F, 1F, 0, 0, 1F)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateRemainingTime (remainMills : Long) {
        val remainSeconds = remainMills / 1000 // ms 이므로 s로 바꿔준다.

        txtMinute.text = "%02d".format(remainSeconds / 60)
        txtSecond.text = "%02d".format(remainSeconds % 60)
    }

    // 같은 인자를 사용할 경우, 단위를 맞춰주는 것이 좋다.
    private fun updateSeekBarTimer (remainMills: Long) {
        seekBarTime.progress = (remainMills / 1000 / 60).toInt()
    }
}