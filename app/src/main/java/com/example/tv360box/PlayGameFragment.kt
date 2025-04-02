package com.example.tv360box

import AuthInterceptor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tv360box.ListGameFragment.Game
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface GamePlayAPI {
    //    @FormUrlEncoded
//    @POST("api/auth/login/no-captcha") // Replace with your endpoint
//    suspend fun login(
//        @Field("username") username: String,
//        @Field("password") password: String
//    ): LoginResponse
    @Headers("Content-Type: application/json")
    @GET("/api/game/enduser/blacknut/access-token-v2")
    suspend fun getGamePlayToken(
    ): LoginResponse


}
class PlayGameFragment : Fragment() {

    private lateinit var webView: WebView
    private var gameData: Game? = null

    private val api2 by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(requireContext()))
            .build()

        Retrofit.Builder()
            .baseUrl("https://viettel-cloud-gaming-customer-api.dft.vn/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GamePlayAPI::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_play_game, container, false)

        webView = view.findViewById(R.id.webView)
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            builtInZoomControls = true
            displayZoomControls = false
            allowFileAccess = true
            allowContentAccess = true
            allowFileAccessFromFileURLs = true
            allowUniversalAccessFromFileURLs = true
        }

        // Set WebViewClient to handle page navigation within the WebView
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url ?: return false)
                return true
            }
        }

        // Load a webpage
        webView.loadUrl("https://cloudgame.vn")
        return view
    }

    private fun getTokenAndLoadWebView() {
        // Use viewLifecycleOwner.lifecycleScope to ensure the coroutine is canceled when the fragment is destroyed
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = api2.getGamePlayToken()
                // Assuming LoginResponse contains a token you need to use
                // You might want to add it to the URL or headers
                val urlWithToken = "https://cloudgame.vn/play/blacknut/e64b0776-6131-499a-b9e7-7dd712cca329?accessToken=${response.accessToken}&refreshToken=${response.refreshToken}&partnerGameId=" // Adjust based on your API needs
                webView.loadUrl(urlWithToken)
            } catch (e: Exception) {
                // Handle error (show toast, error message, etc.)
                // Fallback - load the URL without token if the API fails
                webView.loadUrl("https://cloudgame.vn")
            }
        }
    }

}