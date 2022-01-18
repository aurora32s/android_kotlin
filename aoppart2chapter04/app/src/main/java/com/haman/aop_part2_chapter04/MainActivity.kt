package com.haman.aop_part2_chapter04

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.room.Room
import com.haman.aop_part2_chapter04.model.History
import java.lang.NullPointerException
import java.lang.NumberFormatException
import kotlin.math.exp

class MainActivity : AppCompatActivity() {

    private val txtExpression : TextView by lazy {
        findViewById(R.id.txt_expression)
    }

    private val txtResult : TextView by lazy {
        findViewById(R.id.txt_result)
    }

    private val layoutHistory : View by lazy {
        findViewById(R.id.layout_history)
    }

    // history view 내의 결과 리스트
    private val layoutResult : LinearLayout by lazy {
        findViewById(R.id.layout_result)
    }

    private var isOperator = false // 현재 연산자를 입력하는 경우
    private var hasOperator = false // 이미 연산자를 입력한 경우

    // DB
    lateinit var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // history 데이터베이스 생성
        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "history"
        ).build()
    }

    /**
     * 숫자 및 연산자 버튼 클릭
     */
   fun btnClicked(view: View) {
        when (view.id) {
            R.id.btn_zero -> numberBtnClicked("0")
            R.id.btn_one -> numberBtnClicked("1")
            R.id.btn_two -> numberBtnClicked("2")
            R.id.btn_three -> numberBtnClicked("3")
            R.id.btn_four -> numberBtnClicked("4")
            R.id.btn_five -> numberBtnClicked("5")
            R.id.btn_six -> numberBtnClicked("6")
            R.id.btn_seven -> numberBtnClicked("7")
            R.id.btn_eight -> numberBtnClicked("8")
            R.id.btn_nine -> numberBtnClicked("9")
            R.id.btn_plus -> operatorBtnClicked("+")
            R.id.btn_minus -> operatorBtnClicked("-")
            R.id.btn_multi -> operatorBtnClicked("*")
            R.id.btn_divide -> operatorBtnClicked("/")
            R.id.btn_mod -> operatorBtnClicked("%")
        }
    }

    private fun numberBtnClicked (number : String) {

        if (isOperator) {
            txtExpression.append(" ")
        }
        isOperator = false

        val expression = txtExpression.text.split(" ")
        if (expression.isNotEmpty() && expression.last().length >= 15) {
            // 숫자는 최대 15자리만 입력 가능
            Toast.makeText(this, "15자리 까지만 입력하실 수 있습니다.", Toast.LENGTH_SHORT).show()
            return
        } else if (expression.last().isEmpty() && number == "0") {
            Toast.makeText(this, "0은 가장 앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
        txtExpression.append(number)

        // TODO : txtResult 에 실시간으로 계산 결과를 넣어야 하는 기능
        txtResult.text = calculateExpression()
    }

    private fun operatorBtnClicked (operator : String) {
        // 연산자를 가장 먼저 입력하는 경우에는 무시
        if (txtExpression.text.isEmpty()) {
            return
        }

        when {
            isOperator -> {
                // 이미 연산자를 입력하고 있는 중
                val text = txtExpression.text.toString()
                // 마지막에서부터 n 글자를 제거
                txtExpression.text = text.dropLast(1) + operator
            }
            hasOperator -> {
                // 이미 연산자를 입력하고 숫자를 입력한 경우
                Toast.makeText(this, "연산자는 한 번만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                // 처음 연산자를 입력하는 경우
                txtExpression.append(" $operator")
            }
        }

        // 연산자인 경우에는 초록색으로 표시
        // 어떤 기능을 하는지 찾아보자!
        val ssb = SpannableStringBuilder(txtExpression.text)
        ssb.setSpan(
            ForegroundColorSpan(getColor(R.color.green)),
            txtExpression.text.length - 1,
            txtExpression.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        txtExpression.text = ssb
        isOperator = true
        hasOperator = true
    }

    /**
     * = 버튼 클릭
     */
    fun btnResultClicked(view: View) {
        val expression = txtExpression.text.split(" ")

        if (txtExpression.text.isEmpty() || expression.size == 1) {
            return
        }
        if (expression.size != 3 && hasOperator) {
            // 마지막 피연산자 입력 안함
            Toast.makeText(this, "아직 완성되지 않은 수식입니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (expression[0].isNumber().not() || expression[2].isNumber().not()) {
            Toast.makeText(this, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        // 연산식과 결과를 DB에 저장
        val expressionText = txtExpression.text.toString()
        val resultText = calculateExpression()

        // TODO : DB에 연산 결과값 저장
        // DB 작업은 thread 내에서 작업
        Thread {
            db.historyDao().insertHistory(History(null, expressionText, resultText))
        }.start()

        txtResult.text = ""
        txtExpression.text = resultText

        isOperator = false
        hasOperator = false
    }

    private fun calculateExpression () : String {
        val expression = txtExpression.text.split(" ")

        if (hasOperator.not() || expression.size != 3) {
            return ""
        } else if (expression[0].isNumber().not() || expression[2].isNumber().not()) {
            // 1,3번째 element 가 숫자가 아닌 경우
            return ""
        }

        val exp1 = expression[0].toBigInteger() // 피연산자 1
        val exp2 = expression[2].toBigInteger() // 피연산자 2
        val op = expression[1] // 연산자

        return when(op) {
            "+" -> exp1 + exp2
            "-" -> exp1 - exp2
            "x" -> exp1 * exp2
            "/" -> exp1 / exp2
            "%" -> exp1 % exp2
            else -> ""
        }.toString()
    }

    /**
     * 초기화 버튼 클릭
     */
    fun btnClearClicked(view: View) {
        txtExpression.text = ""
        txtResult.text = ""
        isOperator = false
        hasOperator = false
    }

    /**
     * 연산 기록 보기 버튼 클릭
     */
    fun btnHistoryClicked(view: View) {
        layoutHistory.isVisible = true
        layoutResult.removeAllViews()

        // TODO : DB 에서 모든 기록 가져오기
        Thread{
            db.historyDao().getAll().reversed().forEach {
                // 하나씩 LinearLayout 에 추가
                // ui 작업을 하기 위해 ui thread (main thread) 를 열어줘야 한다.
                // TODO : View 에 모든 기록 할당하기
                runOnUiThread {
                    val historyView = LayoutInflater.from(this).inflate(R.layout.history_item, null, false)
                    historyView.findViewById<TextView>(R.id.txt_expression).text = it.expression
                    historyView.findViewById<TextView>(R.id.txt_result).text = "= ${it.result}"

                    layoutResult.addView(historyView)
                }
            }
        }.start()
    }

    /**
     * 닫기 버튼 클릭
     */
    fun btnCloseHistoryClicked(view: View) {
        layoutHistory.isVisible = false
    }

    /**
     * 기록 삭제 버튼 클릭
     */
    fun btnResetHistoryClicked(view: View) {
        // TODO : View 에서 모든 기록 삭제
        layoutResult.removeAllViews()
        // TODO : DB 에서 모든 기록 삭제
        Thread {
            db.historyDao().deleteAll()
        }.start()
    }
}

// 확장 함수는 activity class 밖에 선언한다.
fun String.isNumber() =
    try {
        toBigInteger()
        true
    } catch (exception : NumberFormatException) {
        false
    }
