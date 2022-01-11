package com.haman.aop_part2_chapter01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private val TAG : String = ".MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // R.layout.activity_main 레이아웃을 해당 Activity 의 main view 로 설정
        // R : 모든 리소스의 주소값(id) 관리
        setContentView(R.layout.activity_main)

        // findViewById(...) : T! <- ! : null 이 되지 않는 값
        // findViewById(...)의 반환값이 제네릭 타입이기 때문에 변수타입을 함께 명시해주어야 한다.
        val heightEditText : EditText = findViewById(R.id.edit_height) // 신장
        val weightEditText : EditText = findViewById(R.id.edit_weight) // 몸무게
        val btnConfirm : Button = findViewById(R.id.btn_confirm) // 확인하기

        // interface 를 람다식으로 생성할 수 있다.
        btnConfirm.setOnClickListener {
            // Log level 별로 출력
            Log.d(TAG,"click confirm button")

            // 유효성 검사
            val txtHeight : String = heightEditText.text.toString()
            val txtWeight : String = weightEditText.text.toString()
            if (txtHeight.isEmpty()) {
                Toast.makeText(this, "신장을 입력해주세요.", Toast.LENGTH_SHORT).show()
                // onCreate 메서드와 중첩으로 선언되었기 때문에 어느 메서드를 종료할 것인지
                // 명시해줄 필요가 있다. -> @(메서드이름)
                return@setOnClickListener
            } else if (txtWeight.isEmpty()) {
                Toast.makeText(this, "몸무게를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val height : Int = txtHeight.toInt()
            val weight : Int = txtWeight.toInt()
            Log.d(TAG, "height : $height / weight : $weight")

            // result 화면으로 이동
            /**
             * intent : 액티비티 시작, 서비스 시작, 브로드캐스트 전달
             * developer document 에서 인텐트 문서 참고
             */
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("height", height) // 신장
            intent.putExtra("weight", weight) // 몸무게
            startActivity(intent)
        }
    }
}