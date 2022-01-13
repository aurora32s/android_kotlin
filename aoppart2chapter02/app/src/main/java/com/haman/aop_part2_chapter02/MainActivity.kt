package com.haman.aop_part2_chapter02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
    private val TAG : String = ".MainActivity"

    // what is lazy?
    private val resetButton : Button by lazy {
        findViewById(R.id.btn_reset)
    }

    private val addButton : Button by lazy {
        findViewById(R.id.btn_add)
    }

    private val runButton : Button by lazy {
        findViewById(R.id.btn_run)
    }

    private val numberPicker : NumberPicker by lazy {
        findViewById(R.id.number_picker)
    }

    private val numberTextViewList : List<TextView> by lazy {
        listOf(
            findViewById(R.id.txt_first_number),
            findViewById(R.id.txt_second_number),
            findViewById(R.id.txt_third_number),
            findViewById(R.id.txt_fourth_number),
            findViewById(R.id.txt_fifth_number),
            findViewById(R.id.txt_sixth_number)
        )
    }

    private var runable = false
    private val numSet = HashSet<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        initRunButton()
        initAddButton()
        initResetButton()
    }

    /**
     * run button 초기화
     */
    private fun initRunButton () {
        runButton.setOnClickListener {
            // 랜덤하게 번호 자동 생성
            val list = getRandomNumber()
            Log.d(TAG, list.toString())
            runable = true

            // 뽑은 데이터를 화면에 표시
            list.forEachIndexed { index, number ->
                addNumberTxt(index, number)
            }
        }
    }

    /**
     * add button 초기화
     */
    private fun initAddButton () {
        addButton.setOnClickListener {
            // 이미 자동생성 시작으로 5개의 숫자를 다 선택한 경우
            if (runable) {
                Toast.makeText(this, "초기화 후에 다시 선택해주세요.", Toast.LENGTH_SHORT).show()
                // 어느 함수를 탈출하는지 명시적으로 표시
                return@setOnClickListener
            }
            // 이미 사용자가 5개의 숫자를 다 선택한 경우
            if (numSet.size >= 6) {
                Toast.makeText(this, "번호는 6개까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // 이미 선택된 번호를 선택한 경우
            if (numSet.contains(numberPicker.value)) {
                Toast.makeText(this, "이미 선택한 번호입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            addNumberTxt(numSet.size, numberPicker.value)
            numSet.add(numberPicker.value)
        }
    }

    private fun initResetButton () {
        resetButton.setOnClickListener {
            // 선택한 숫자 초기화
            numSet.clear()
            numberTextViewList.forEach{
                // 숫자 textView 초기화
                it.isVisible = false
            }
            runable = false
        }
    }

    private fun getRandomNumber() : List<Int> {
        val numberList = mutableListOf<Int>().apply {
            for (i in 1..45) {
                if (numSet.contains(i)) continue
                this.add(i)
            }
        }
        // 리스트 무작위로 shuffle
        numberList.shuffle()
        val newList = numSet + numberList.subList(0, 6 - numSet.size)
        return newList.sorted() // from-to
    }

    private fun addNumberTxt (index: Int, number: Int) {
        val txtView = numberTextViewList[index]
        txtView.text = number.toString()
        txtView.isVisible = true

        val background = when(number) {
            in 1..10 -> R.drawable.circle_yello
            in 11..20 -> R.drawable.circle_blue
            in 21..30 -> R.drawable.circle_red
            in 31..40 -> R.drawable.circle_gray
            else -> R.drawable.circle_green
        }
        txtView.background = ContextCompat.getDrawable(this,background)
    }
}