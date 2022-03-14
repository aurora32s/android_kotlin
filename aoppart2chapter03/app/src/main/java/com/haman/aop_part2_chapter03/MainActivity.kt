package com.haman.aop_part2_chapter03

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private val numberPickerFirst : NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.number_picker_first)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPickerSecond : NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.number_picker_second)
            .apply{
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPickerThird : NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.number_picker_third)
            .apply{
                minValue = 0
                maxValue = 9
            }
    }

    private val btnConfirm : AppCompatButton by lazy {
        findViewById(R.id.btn_confirm)
    }
    private val btnChangePwd : AppCompatButton by lazy {
        findViewById(R.id.btn_change_pwd)
    }

    override fun onStart() {
        super.onStart()
        Log.d(".MainActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(".MainActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(".MainActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(".MainActivity", "onStop")
    }

    // true : 비밀번호 변경 모드, false : 비밀번호 등록 하기
    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 각 number picker 에 접근을 해서 lazy 하게 초기화 한다.
        numberPickerFirst
        numberPickerSecond
        numberPickerThird

        // 비밀번호 확인 버튼 클릭
        btnConfirm.setOnClickListener {
            if (changePasswordMode) { // 비밀번호 변경 모드 중에는 비밀번호 확인 불가
                Toast.makeText(this, "비밀번호 변경 중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (checkPwd()) { // 패스워드가 일치하는 경우
                // TODO 다이어리 페이지 작성 후에 넘겨주어야 함.
                startActivity(Intent(this, DiaryActivity::class.java))
            }
        }

        // 비밀번호 변경 버튼 클릭
        btnChangePwd.setOnClickListener {
            if (changePasswordMode) {
                // 비밀번호 저장
                val passwordPref = getSharedPreferences("password", Context.MODE_PRIVATE)

                /**
                 * java 에서는
                 * SharedPreference.Editor edit = passwordPref.edit()
                 * edit.putString()
                 * commit() : 동기적으로 저장 / apply() : 비동기적으로 저장(오래걸리는 작업)
                 * 의 작업이 필요
                 */

                // 작업이 모두 끝내고 commit 과 apply 중 어느 작업을 할 것인지, 인자를 통해 전달
                passwordPref.edit(commit = true) {
                    val passwordFromUser = "${numberPickerFirst.value}${numberPickerSecond.value}${numberPickerThird.value}"
                    putString("password", passwordFromUser)
                }
                changePasswordMode = false
                // 비밀번호 변경이 완료되었다는 것을 알려주기 위해 색 변경
                btnChangePwd.setBackgroundColor(Color.BLACK)
            } else {
                // changePasswordMode 활성화 :: 비밀번호가 맞는지 확인(비밀번호가 맞는 경우에만 비밀번호 변경 가능)
                if (checkPwd()) { // 비밀번호가 일치하는 경우
                    changePasswordMode = true
                    Toast.makeText(this, "변경할 패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    // 비밀번호 변경 중이라는 것을 알려주기 위해 색 변경
                    btnChangePwd.setBackgroundColor(Color.RED)
                }
            }
        }
    }

    private fun checkPwd () : Boolean {
        // 다른 앱과도 연동이 가능한 sharedPreference, MODE_PRIVATE : 다른 앱과 공유 불가
        val passwordPref = getSharedPreferences("password", Context.MODE_PRIVATE)
        val passwordFromUser = "${numberPickerFirst.value}${numberPickerSecond.value}${numberPickerThird.value}"
        val passwordFromDevice = passwordPref.getString("password", "000")

        return if (passwordFromUser == passwordFromDevice) {
            // 패스워드 일치
            true
        } else {
            // 패스워드 불일치
            AlertDialog.Builder(this)
                .setTitle("실패!")
                .setMessage("비밀번호가 잘못 되었습니다.")
                // 마지막 인자가 람다식인 경우 함수 호출문 바깥에 선언하자.
                .setPositiveButton("닫기"){_,_ -> }
                .create()
                .show()
            false
        }
    }
}