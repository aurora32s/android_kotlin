package com.haman.aop_part3_chapter01

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private val txtResult : TextView by lazy { findViewById(R.id.txt_result) }
    private val txtToken : TextView by lazy { findViewById(R.id.txt_token) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFirebase()
        updateResult()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // 기존에 열린 activity 가 가지고 있는 intent 에서 새로 호출된 intent 로 변경한다.
        setIntent(intent)
        updateResult(true)
    }

    private fun initFirebase () {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                // token 발급 완료
                if (task.isSuccessful) {
                    val token = task.result
                    txtToken.text = token.toString()
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun updateResult (isNewIntent : Boolean = false) {
        // 앱이 새로 생성된 경우 : false
        // 앱이 켜져 있는데 notification 에 의해 업데이트 된 경우
        txtResult.text = (intent.getStringExtra("notificationType") ?: "앱 런처") +
            if (isNewIntent) {
                "(으)로 갱신했습니다."
            } else {
                "(으)로 실행했습니다."
            }
    }

    companion object {
        private const val TAG = ".MainActivity"
    }
}