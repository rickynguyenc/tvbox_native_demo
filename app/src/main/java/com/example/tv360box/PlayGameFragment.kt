package com.example.tv360box

import AuthInterceptor
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tv360box.ListGameFragment.Game
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

data class PlayToken (
    val expiresAt: Long,
    val expiresIn: Long,
    val accessToken: String,
    val refreshToken: String,
)

interface GamePlayAPI {
    @Headers("Content-Type: application/json")
    @GET("api/game/enduser/blacknut/access-token-v2")
    suspend fun getGamePlayToken(): PlayToken
}

class PlayGameActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var gameData: Game? = null

    private val api2 by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(this))
            .build()

        Retrofit.Builder()
            .baseUrl("https://viettel-cloud-gaming-customer-api.dft.vn/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GamePlayAPI::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_play_game) // Note: You'll need to create this layout

        // Retrieve the GameItem from intent
        gameData = intent.getParcelableExtra("gameItem")
            ?: throw IllegalArgumentException("GameItem is required")

        webView = findViewById(R.id.webview)
        setupWebView()
        getTokenAndLoadWebView()
    }

    private fun setupWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            allowContentAccess = true
            allowFileAccess = true
            useWideViewPort = true
            loadWithOverviewMode = true
            javaScriptCanOpenWindowsAutomatically = true
            mediaPlaybackRequiresUserGesture = false
            setSupportMultipleWindows(true)
        }

        webView.webChromeClient = WebChromeClient()

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                return false
            }
        }
    }

    private fun getTokenAndLoadWebView() {
        lifecycleScope.launch {
            try {
                val response = api2.getGamePlayToken()
                val urlWithToken = "https://cloudgame.vn/play/blacknut/${gameData?.id}?accessToken=${response.accessToken}&refreshToken=${response.refreshToken}&partnerGameId=${gameData?.partnerGameId}"
                webView.loadUrl(urlWithToken)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        webView.destroy()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}