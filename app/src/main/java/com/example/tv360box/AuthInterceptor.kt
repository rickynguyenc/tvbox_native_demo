import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Get token from SharedPreferences
        val sharedPref = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val accessToken = sharedPref.getString("access_token", null)

        // If token exists, add it to the request
        return if (!accessToken.isNullOrEmpty()) {
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(originalRequest) // Proceed without token
        }
    }
}