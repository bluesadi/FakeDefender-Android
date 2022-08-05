package cn.bluesadi.fakedefender.util.network

import android.net.SSLCertificateSocketFactory
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.util.ToastUtil
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.xuexiang.xui.XUI
import com.xuexiang.xui.utils.ResUtils
import org.json.JSONObject
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import com.android.volley.DefaultRetryPolicy
import com.android.volley.toolbox.HurlStack
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*


/**
 *
 * @author 34r7hm4n
 * @since 2022/3/28 10:06
 */
class Http(val url: String = COMMON_URL) {

    companion object {
        const val COMMON_URL = "https://hk.crackpoly.life:1443"
        //const val COMMON_URL = "http://10.136.126.13:5000"

        fun validate(jsonObject: JSONObject) : Boolean{
            return jsonObject.has("code") && jsonObject.getInt("code") == 0
        }
    }

    private val queue: RequestQueue by lazy {
        val trustAllCerts: Array<TrustManager> = arrayOf(object : X509TrustManager {
            override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
            override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<X509Certificate>? { return null }
        })
        HttpsURLConnection.setDefaultSSLSocketFactory(SSLContext.getInstance("SSL").apply {
            init(null, trustAllCerts, SecureRandom())
        }.socketFactory)
        val allHostsValid = HostnameVerifier { _, _ -> true }
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid)
        CookieHandler.setDefault(CookieManager(null, CookiePolicy.ACCEPT_ALL))
        Volley.newRequestQueue(XUI.getContext())
    }

    fun post(jsonObject: JSONObject, listener: Response.Listener<JSONObject>, checkStatus: Boolean = true){
        queue.add(
            JsonRequestWithCookie(Request.Method.POST, url, jsonObject, { response ->
                if(validate(response) || !checkStatus) {
                    listener.onResponse(response)
                }else{
                    ToastUtil.error(response.getString("msg"))
                }
            }, { error ->
                error.networkResponse?.let {
                    ToastUtil.error(ResUtils.getString(R.string.http_request_error)
                            + "(${it.statusCode}) ${error.message}")
                } ?: ToastUtil.error(ResUtils.getString(R.string.http_request_error) + error.message)
            }).setRetryPolicy(
                DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
            )
        )
    }

}