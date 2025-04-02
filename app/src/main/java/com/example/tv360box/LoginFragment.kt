package com.example.tv360box

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.io.IOException
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers

data class LoginResponse (
    val responseMessage: String,
    val responseCode: Long,
    val responseDate:String,
    val accessToken: String,
    val refreshToken:String,
)
data class LoginRequest(
    val username: String,
    val password: String
)
interface LoginApi {
    //    @FormUrlEncoded
//    @POST("api/auth/login/no-captcha") // Replace with your endpoint
//    suspend fun login(
//        @Field("username") username: String,
//        @Field("password") password: String
//    ): LoginResponse
    @Headers("Content-Type: application/json")
    @POST("api/auth/login/no-captcha")
    suspend fun login(
        @Body requestBody: LoginRequest
    ): LoginResponse


}
val retrofit = Retrofit.Builder()
    .baseUrl("https://viettel-cloud-gaming-customer-api.dft.vn/") // Replace with your base URL
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val api = retrofit.create(LoginApi::class.java)
class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        view.findViewById<Button>(R.id.loginButton).setOnClickListener {
//            loginReq()
            activity?.runOnUiThread {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ListGameFragment())
                    .commit()
            }
        }

        return view
    }

    private fun loginReq() {

        lifecycleScope.launch {
            try {
                val response = api.login(
                    LoginRequest(
                        "0983000001",
                        "pw123aA@"
                    )
                )
                val accessToken = response.accessToken
                println("Access Token===================: $accessToken")
                saveAccessToken(accessToken) // Save it (see previous response)
                activity?.runOnUiThread {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ListGameFragment())
                        .commit()
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
                e.printStackTrace()
            }
        }
    }
    private fun saveAccessToken(accessToken: String) {
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("access_token", accessToken)
            apply() // Use apply() for asynchronous saving
        }
    }
}