package cn.bluesadi.fakedefender.util.network

import cn.bluesadi.fakedefender.data.Config
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

/**
 *
 * @author 34r7hm4n
 * @since 2022/4/16 18:02
 */
class JsonRequestWithCookie(
    method: Int, url: String, jsonRequest: JSONObject, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener
) : JsonObjectRequest(method, url, jsonRequest, listener, errorListener) {

    private val SET_COOKIE_KEY = "Set-Cookie"
    private val COOKIE_KEY = "Cookie"

    override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
        response?.headers?.takeIf{ it.containsKey(SET_COOKIE_KEY) }?.let { headers ->
            Config.cookie = headers[SET_COOKIE_KEY]
        }
        return super.parseNetworkResponse(response)
    }

    override fun getHeaders(): MutableMap<String, String> {
        val headers = super.getHeaders().toMutableMap()
        Config.cookie?.let {
            headers[COOKIE_KEY] = it
        }
        return headers
    }

}