package com.haman.aop_part3_chapter03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO : Step 0 initialize view
        initOnOffButton()
        initChangeAlarmTimeButton()

        // TODO : Step 1 get data from SharedPreference

        // TODO : Step 2 binding data to view
    }

    private fun initOnOffButton () {
        val onOffButton : Button = findViewById(R.id.onoffButton)
        onOffButton.setOnClickListener {
            // TODO : 데이터를 확인한다.

            // TODO : on / off 에 따라 작업 처리 -> on(알람을 등록), off(알람을 제거)

            // TODO : 데이터를 저장한다.
        }
    }

    private fun initChangeAlarmTimeButton () {
        val changeAlarmTimeButton : Button = findViewById(R.id.changeAlarmTimeButton)
        changeAlarmTimeButton.setOnClickListener {
            // TODO : 현재시간을 가져온다.

            // TODO : TimePickerDialog 를 사용해서 알람 시간을 사용자로부터 입력 받는다.

            // TODO : 사용자가 선택한 시간으로 데이터를 저장한다.

            // TODO : 선택된 시간으로 view 를 업데이트 한다.

            // TODO : 기존에 작업중인 알람이 있다면 삭제한다.
        }
    }
}