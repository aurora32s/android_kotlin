package com.haman.aop_part3_chapter02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }

    private val loadingBar : ProgressBar by lazy {
        findViewById(R.id.loading_bar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initData()
    }

    private fun initViews () {
        viewPager.setPageTransformer { page, position ->
            // -2 -1 0(실제 디바이스에서 보이는 화면) 1 2
            // translation(가로/세로), scale, alpha -> 값을 조작해서 효과 구현
            when {
                position.absoluteValue >= 1F -> {
                    page.alpha = 0F
                }
                position == 0F -> {
                    // 현재 보여지고 있는 페이지
                    page.alpha = 1F
                }
                else -> {
                    // 0의 좌우에 인접한 뷰들이 움직이는 경우
                    page.alpha = 1F - position.absoluteValue * 2
                }
            }
        }
    }

    private fun initData() {
        // remote config 는 기본 12 시간 동안은 캐시를 이용한다.
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                // 개발용에서는 바로바로 업데이트할 수 있게, interval 를 줄여준다.
                minimumFetchIntervalInSeconds = 0
            }
        )
        // 실무에서는 remote config 에서 데이터를 받아오는데 실패했을 경우, 화면에 보여줄 데이터를
        // remoteConfig.setDefaultsAsync() 을 통해 세팅해준다. (지금은 연습이니 패스..)
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            loadingBar.visibility = View.GONE
            // fetchAndActivate 가 비동기적으로 수행되기 때문에 listener(callback method)를 지정해주어야 한다.
            if (it.isSuccessful) {
                // 성공적으로 작업이 완료되었을 경우에만 수행 한다.
                // gson 라이브러리를 사용하면 json 을 파싱하기 쉽지만, 여기서는 직접 구현해보자!
                val quotes = parseQuotesJson(remoteConfig.getString("quotes"))
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")

                displayQuotesPager(quotes, isNameRevealed)
            }
        }
    }

    private fun displayQuotesPager (quotes : List<Quote>, isNameRevealed : Boolean) {
        val adapter = QuotePagerAdapter(
            quotes = quotes,
            isNameRevealed = isNameRevealed
        )
        viewPager.adapter = adapter
        viewPager.setCurrentItem(adapter.itemCount/2, false)
    }

    private fun parseQuotesJson(json: String): List<Quote> {
        val jsonArray = JSONArray(json)
        var jsonList = emptyList<JSONObject>()

        for (index in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(index)
            jsonObject?.let {
                jsonList = jsonList + it
            }
        }

        return jsonList.map {
            Quote(
                it.getString("quote"),
                it.getString("name")
            )
        }
    }
}