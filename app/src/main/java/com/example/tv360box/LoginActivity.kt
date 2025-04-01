package com.example.tv360box

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Set the theme before super.onCreate()
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            runOnUiThread {
                // Navigate to ListGameScreen on successful login
                supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, ListGameFragment())
                    .commit()
            }
//            loginReq()
        }
    }

    private fun loginReq() {
        val client = OkHttpClient()
        val url = "https://gamedemo.vn/login"

        val loginData = mapOf(
            "username" to "demoplay",
            "password" to "1111"
        )

        val json = Gson().toJson(loginData)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    // Handle error
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        // Navigate to ListGameScreen on successful login
                        supportFragmentManager.beginTransaction()
                            .replace(android.R.id.content, ListGameFragment())
                            .commit()
                    }
                }
            }
        })
    }
}