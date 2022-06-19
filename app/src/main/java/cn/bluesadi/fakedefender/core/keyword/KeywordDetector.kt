package cn.bluesadi.fakedefender.core.keyword

import cn.bluesadi.fakedefender.core.alarm.BubbleAlarm
import cn.bluesadi.fakedefender.core.risklevel.RiskLevelManager
import cn.bluesadi.fakedefender.util.d
import cn.bluesadi.fakedefender.util.media.AudioRecordUtil
import cn.bluesadi.fakedefender.util.media.SensitiveWordDetector
import cn.xfyun.api.RtasrClient
import cn.xfyun.service.rta.AbstractRtasrWebSocketListener
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import okhttp3.Response
import okhttp3.WebSocket
import okio.ByteString.Companion.toByteString
import java.nio.ByteBuffer
import java.lang.Exception

/**
 *
 * @author 34r7hm4n
 * @since 2022/5/7 19:25
 */
object KeywordDetector {

    var isStart = false
    private var client: RtasrClient? = null
    private var recorder: AudioRecordUtil? = null
    private var cooldown = false

    fun start(){
        isStart = true
        client = RtasrClient.Builder().addPunc().signature("d4be95ae", "bb89d64746dd190ea3307aeb0f56c2e3").build()
        val socket = client!!.newWebSocket(object : AbstractRtasrWebSocketListener() {
            override fun onClosed() {

            }

            override fun onSuccess(webSocket: WebSocket?, text: String) {
                getContent(text)?.let { statement ->
                    if(!cooldown){
                        cooldown = true
                        SensitiveWordDetector.detect(statement)?.let { word ->
                            RiskLevelManager.sensitive = true
                            BubbleAlarm.sensitiveKeywordStartingAlarm(word)
                        }
                    }
                }
            }

            override fun onFail(webSocket: WebSocket?, t: Throwable?, response: Response?) {

            }

            override fun onBusinessFail(webSocket: WebSocket?, text: String?) {
            }
        })
        recorder = AudioRecordUtil().apply {
            setOnRecordListener(object : AudioRecordUtil.OnRecordListener{
                override fun readByte(data: ByteArray, size: Int) {
                    val result = socket.send(ByteBuffer.wrap(data).toByteString())
                    //d("我听到了: ${data.size}, $result")
                }
            })
            startRecord()
        }
    }

    fun stop(){
        if(isStart){
            isStart = false
            client?.sendEnd()
            recorder?.stopRecord()
            client = null
            recorder = null
        }
    }

    // 把转写结果解析为句子
    fun getContent(message: String): String? {
        val resultBuilder = StringBuffer()
        try {
            var messageObj: JSONObject = JSON.parseObject(message)
            messageObj = messageObj.getJSONObject("data")
            val cn = messageObj.getJSONObject("cn")
            val st = cn.getJSONObject("st")
            val rtArr = st.getJSONArray("rt")
            for (i in 0 until rtArr.size) {
                val rtArrObj = rtArr.getJSONObject(i)
                val wsArr = rtArrObj.getJSONArray("ws")
                for (j in 0 until wsArr.size) {
                    val wsArrObj = wsArr.getJSONObject(j)
                    val cwArr = wsArrObj.getJSONArray("cw")
                    for (k in 0 until cwArr.size) {
                        val cwArrObj = cwArr.getJSONObject(k)
                        val wStr = cwArrObj.getString("w")
                        resultBuilder.append(wStr)
                    }
                }
            }
        } catch (e: Exception) {
            return null
        }
        return resultBuilder.toString()
    }

}