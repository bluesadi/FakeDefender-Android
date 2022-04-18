package cn.bluesadi.fakedefender.network

import android.graphics.Bitmap
import android.graphics.Rect
import cn.bluesadi.fakedefender.face.DetectionRecord
import cn.bluesadi.fakedefender.face.MonitorManager
import cn.bluesadi.fakedefender.util.ImageUtil
import cn.bluesadi.fakedefender.util.network.Http
import com.android.volley.Response
import org.json.JSONObject
import java.util.*

/**
 *
 * @author 34r7hm4n
 * @since 2022/3/28 10:19
 */
object NetworkServices {

    fun getAppCategory(pkgName: String, listener: Response.Listener<Pair<Int, String>>) {
        Http(Http.COMMON_URL + "/category").post(JSONObject().apply {
            put("pkgName", pkgName)
        }, { response ->
            listener.onResponse(
                Pair(
                    response.getInt("categoryId"),
                    response.getString("categoryName")
                )
            )
        })
    }

    fun getVerifyCode(phoneNumber: String, listener: Response.Listener<JSONObject>){
        Http(Http.COMMON_URL + "/user/get_verif_code").post(JSONObject().apply {
            put("phone", phoneNumber)
        }, listener)
    }

    fun register(phoneNumber: String, verifyCode: String, listener: Response.Listener<JSONObject>){
        Http(Http.COMMON_URL + "/register").post(JSONObject().apply {
            put("phoneNumber", phoneNumber)
            put("verifyCode", verifyCode)
        }, listener)
    }

    fun login(phoneNumber: String, verifyCode: String, listener: Response.Listener<JSONObject>){
        Http(Http.COMMON_URL + "/user/login").post(JSONObject().apply {
            put("phone", phoneNumber)
            put("code", verifyCode)
        }, listener)
    }

    fun checkState(listener: Response.Listener<JSONObject>){
        Http(Http.COMMON_URL + "/user/check_state").post(JSONObject(), listener, false)
    }

    fun predict(bitmap: Bitmap, listener: Response.Listener<DetectionRecord>){
        val timestamp = System.currentTimeMillis()
        Http(Http.COMMON_URL + "/user/predict").post(JSONObject().apply {
            put("uuid", UUID.randomUUID().toString())
            put("image", ImageUtil.convertToBase64(bitmap))
        }, {
            val result = it.getJSONObject("data")
            println(result)
            val faceNum = result.getInt("faceNum")
            val faces = result.getJSONArray("faces")
            val facesScores = mutableListOf<DetectionRecord.FaceScore>()
            for(i in 0 until faceNum){
                val face = faces[i] as JSONObject
                val rect = Rect(
                    face.getInt("x1"),
                    face.getInt("y1"),
                    face.getInt("x2"),
                    face.getInt("y2")
                )
                val score = (face.getDouble("score") * 100).toInt()
                facesScores.add(DetectionRecord.FaceScore(rect, score))
            }
            val record = DetectionRecord(timestamp, facesScores, bitmap)
            MonitorManager.update(record)
            listener.onResponse(record)
        })
    }

}