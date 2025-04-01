package com.example.tv360box

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class LoginFragment : Fragment() {

    private val client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        view.findViewById<Button>(R.id.loginButton).setOnClickListener {
            loginReq(mapOf("username" to "0983000001", "password" to "pw123aA@"),
                "https://viettel-cloud-gaming-customer-api.dft.vn/api/auth/login/no-captcha")
        }

        return view
    }

    private fun loginReq(credentials: Map<String, String>, url: String) {
//        val formBody = FormBody.Builder()
//            .add("username", credentials["username"] ?: "")
//            .add("password", credentials["password"] ?: "")
//            .build()
        val jsonBody = """
    {"username": "${credentials["username"] ?: ""}", "password": "${credentials["password"] ?: ""}"}
""".trimIndent()
        val body = jsonBody.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        activity?.runOnUiThread {
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, ListGameFragment())
                                .commit()
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}