package com.haman.aop_part2_chapter08

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.ContentLoadingProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {
    private val TAG by lazy { ".MainActivity" }

    private val webView : WebView by lazy {
        findViewById(R.id.webview)
    }

    private val editTextForAddress : EditText by lazy {
        findViewById(R.id.edit_address)
    }

    private val btnHome : ImageButton by lazy {
        findViewById(R.id.btn_home)
    }

    private val btnBack : ImageButton by lazy {
        findViewById(R.id.btn_back)
    }

    private val btnForward : ImageButton by lazy {
        findViewById(R.id.btn_forward)
    }

    private val refreshLayout : SwipeRefreshLayout by lazy {
        findViewById(R.id.swiperefreshlayout)
    }

    private val progressBar : ContentLoadingProgressBar by lazy {
        findViewById(R.id.progress_bar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        bindViews()
    }

    // SuppressLint : 경고 무시
    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews () {
        // the default behavior is for Android to launch an app that handles URLs.
        // 따라서 휴대폰에 설정된 default 웹 브라우저를 호출한다.
        // you can override this behavior for your WebView, so links open within your WebView.
        // You can then allow the user to navigate backward and forward through their web page
        // history that's maintained by your WebView.
//        webView.webViewClient = WebViewClient()
        // 웹 뷰 내부의 동작(javascript 소스)를 허용하기 위해 명시적으로 허용해주어야 한다.(보안상의 문제)
        // 스크립트 공격으로부터 취약해질 수 있다. (추가적인 처리 필요)
//        webView.settings.javaScriptEnabled = true

        // ERR_CLEARTEXT_NOT_PERMITTED : https 가 아닌 웹 사이트에 접근하는 경우 발생하는 에러
        // Manifest 에 추가적인 설정 필요
//        webView.loadUrl("http://www.google.com")

        // 위에서 webView 를 세번이나 참조하므로, apply 로 한번에 정리
      webView.apply {
          webViewClient = WebViewClient()
          webChromeClient = WebChromeClient()
          settings.javaScriptEnabled = true
          loadUrl(DEFAULT_URL)
      }
    }

    private fun bindViews () {
        // 주소 이동 시, Enter 키를 누르면 해당 URL 로 이동할 수 있도록 구현
        // v : The view that was clicked
        // actionId : 오른쪽 하단 버튼에 설정된 action 타입
        // return : return true if you have consumed the action, else false
        // actionDone 일 경우 키보드가 닫히는 이벤트가 필요하기 때문에 반드시 false 를 반환해주어야 한다.
        editTextForAddress.setOnEditorActionListener { v, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    val loadingUrl = v.text.toString()
                    if (!URLUtil.isNetworkUrl(loadingUrl)) { // http 또는 https 로 시작하면 true 반환
                        webView.loadUrl("http://$loadingUrl")
                    } else {
                        webView.loadUrl(v.text.toString())
                    }
                    false
                }
                else -> {
                    false
                }
            }
        }

        btnBack.setOnClickListener{
            webView.goBack()
        }
        btnForward.setOnClickListener {
            webView.goForward()
        }
        btnHome.setOnClickListener {
            webView.loadUrl(DEFAULT_URL)
        }

        refreshLayout.setOnRefreshListener {
            // swipe 를 당겨서 로딩 바가 보일 때
            webView.reload()

            // refresh 로딩 바를 제거하는데는 추가적인 코드가 필요하다.
            // 사라지는 건 화면 이동이 완료되었을 때
        }
    }

    /**
     * back button click 시 호출되는 오버라이드 메서드
     */
    override fun onBackPressed() {
//        super.onBackPressed() <- 앱 종료
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    // 상위 클래스의 프로퍼티에 접근할 수 있도록 내부 클래스로 선언한다.
    // webViewClient : Content 로딩과 관련된 정보를 주로 사용할 때
    // webChromeClient : 브라우저 관점(?)에서의 기능 제공 <- 더 많고 세부적인 기능 제공
    inner class WebViewClient : android.webkit.WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            refreshLayout.isRefreshing = false
            progressBar.hide()

            btnBack.isEnabled = webView.canGoBack()
            btnForward.isEnabled = webView.canGoForward()
            editTextForAddress.setText(url)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            // page 이동을 시작했을 때
            progressBar.show()
            super.onPageStarted(view, url, favicon)
        }
    }

    inner class WebChromeClient : android.webkit.WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            progressBar.progress = newProgress
        }
    }

    companion object {
        private const val DEFAULT_URL = "http://www.google.com"
    }
}