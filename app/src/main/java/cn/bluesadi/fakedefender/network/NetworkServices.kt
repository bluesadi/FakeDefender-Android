package cn.bluesadi.fakedefender.network

import android.graphics.Bitmap
import android.graphics.Rect
import cn.bluesadi.fakedefender.adapter.DetectionRecordListAdapter
import cn.bluesadi.fakedefender.core.DetectionRecord
import cn.bluesadi.fakedefender.core.MonitorManager
import cn.bluesadi.fakedefender.data.AdvancedSettings
import cn.bluesadi.fakedefender.util.SystemStatus
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
        Http(Http.COMMON_URL + "/user/get_app_info").post(JSONObject().apply {
            put("pkgName", pkgName)
        }, { response ->
            val code = response.getInt("code")
            listener.onResponse(
                if(code == 0) {
                    val data = response.getJSONObject("data")
                    Pair(
                        data.getInt("categoryId"),
                        data.getString("categoryName")
                    )
                }else Pair(-1, "")
            )
        }, false)
    }

    fun getVerifyCode(phoneNumber: String, listener: Response.Listener<JSONObject>){
        //Http("http://124.222.27.211:5000/user/get_verif_code").post(JSONObject().apply {
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

    fun emailAlarm(phone: String, email: String, bitmap: Bitmap){
        Http(Http.COMMON_URL + "/user/email_alarm").post(JSONObject().apply {
            put("phone", phone)
            put("target_email", email)
            put("img", ImageUtil.convertToBase64(bitmap))
        }, {})
    }

    fun messageAlarm(phone: String, targetPhone: String){
        Http(Http.COMMON_URL + "/user/SMS_alarm").post(JSONObject().apply {
            put("phone", phone)
            put("target_phone", targetPhone)
        }, {})
    }

    fun predict(bitmap: Bitmap, listener: Response.Listener<DetectionRecord>, manual: Boolean){
        val timestamp = System.currentTimeMillis()
        Http(Http.COMMON_URL + "/user/predict").post(JSONObject().apply {
            put("uuid", UUID.randomUUID().toString())
            put("image", ImageUtil.convertToBase64(bitmap))
            put("area", 12000)
            put("yaw", 40)
            put("blur", when(AdvancedSettings.filterStrategy){
                0 -> 5
                2 -> 100
                else -> 25
            })
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
            if(manual){
                DetectionRecordListAdapter.INSTANCE.addRecord(record, manual)
            }else{
                MonitorManager.update(record)
            }
            listener.onResponse(record)
        })
    }

}