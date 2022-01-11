package com.haman.aop_part2_chapter01

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow

/**
 * BMI 결과값 표시 화면
 */
class ResultActivity : AppCompatActivity() {
    private val TAG = ".ResultActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val height = intent.getIntExtra("height", 0) // 신장
        val weight = intent.getIntExtra("weight", 0) // 몸무게
        Log.d(TAG, "height : $height / weight : $weight")

        // 자바의 Math.pow(a,b) 와 동일한 kotlin 라이브러리
        val bmi = height / (height / 100.0).pow(2.0)
        val result = when {
            35.0 <= bmi -> "고도비만"
            30.0 <= bmi -> "중정도비만"
            25.0 <= bmi -> "경도비만"
            23.0 <= bmi -> "과제충"
            18.5 <= bmi -> "정상체중"
            else -> "저체중"
        }

        val txtBmi : TextView = findViewById(R.id.txt_bmi)
        val txtResult : TextView = findViewById(R.id.txt_result)

        // 데이터 바인딩
        txtBmi.text = bmi.toString()
        txtResult.text = result
    }
}