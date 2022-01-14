package com.haman.aop_part2_chapter03

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity : AppCompatActivity() {
    private val TAG = ".DiaryActivity"

    private val editDiary : EditText by lazy {
        findViewById(R.id.edit_diary)
    }

    // handler 와 looper 에 대해서 학습 필요
    private val handler = Handler(
        // main looper : 메인 스레드에 연결된 스레드 생성
        Looper.getMainLooper()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val diaryPref = getSharedPreferences("diary", Context.MODE_PRIVATE)
        editDiary.setText(diaryPref.getString("context", ""))

        // 실시간으로 저장하면 리소스가 많으 들기 때문에 적다가 텀이 생기면 저장
        val runnable = Runnable {
            // commit 이 false 인 경우에는 apply 동작(비동기적으로 내용 저장)
            diaryPref.edit(commit = false) {
                putString("context", editDiary.text.toString())
            }
            Log.d(TAG, "success save context >> ${editDiary.text.toString()}")
        }

        editDiary.addTextChangedListener {
            Log.d(TAG, "Text Changed > $it")
            // 생성된 runnable 제거
            handler.removeCallbacks(runnable)
            // handler : 스레드를 생성했을 때 메인 스레드에서 관리할 수 있는 기능 제공(메인 스레드 <-> 새로 생성된 스레드)
            handler.postDelayed(runnable, 500) // 0.5초
        }
    }
}