package cn.bluesadi.fakedefender.core

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.work.*
import cn.bluesadi.fakedefender.fragment.MonitorMainFragment
import androidx.core.app.NotificationCompat
import cn.bluesadi.fakedefender.core.alarm.BubbleAlarm
import cn.bluesadi.fakedefender.core.alarm.EmailAlarm
import cn.bluesadi.fakedefender.core.alarm.PopWindowAlarm
import cn.bluesadi.fakedefender.core.alarm.SMSAlarm
import cn.bluesadi.fakedefender.core.keyword.KeywordDetector
import cn.bluesadi.fakedefender.core.risklevel.RiskLevel
import cn.bluesadi.fakedefender.core.risklevel.RiskLevelManager
import cn.bluesadi.fakedefender.data.AdvancedSettings
import cn.bluesadi.fakedefender.data.AlarmSettings
import cn.bluesadi.fakedefender.network.NetworkServices
import cn.bluesadi.fakedefender.util.FaceDetector
import cn.bluesadi.fakedefender.util.d
import com.xuexiang.xui.XUI
import com.xuexiang.xui.utils.ResUtils
import kotlinx.coroutines.delay
import java.lang.Integer.max
import kotlin.math.min

/**
 *
 * @author 34r7hm4n
 * @since 2022/4/11 9:24
 */
class MonitorWorker(context: Context, workerParameters: WorkerParameters) : CoroutineWorker(context, workerParameters) {

    companion object {

        const val UNIQUE_WORK_NAME = "MONITOR"

        const val CHANNEL_ID = "FAKE_DEFENDER"

        const val CHANNEL_NAME = "FAKE_DEFENDER_MONITOR"

        const val NOTIFICATION_ID = 1

        fun buildWorkRequest() : OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<MonitorWorker>().build()
        }

    }

    private val handler = Handler(Looper.getMainLooper())
    private var hits = 0

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun createNotificationChannel(){
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE)
        channel.apply {
            lightColor = Color.BLUE
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            setAllowBubbles(true)
        }
        val service = XUI.getContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private suspend fun switchToForeground(){
        createNotificationChannel()
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(cn.bluesadi.fakedefender.R.drawable.ic_logo)
            .setContentTitle(ResUtils.getString(cn.bluesadi.fakedefender.R.string.notification_title))
            .setContentText(ResUtils.getString(cn.bluesadi.fakedefender.R.string.notification_text))
            .build()
        setForeground(
            ForegroundInfo(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION
            )
        )
    }

    private fun sendDetectionRequest(bitmap: Bitmap, manual: Boolean){
        NetworkServices.predict(bitmap, {
            handler.post {
                MonitorMainFragment.instance?.get()?.updateScore(MonitorStats.riskScore)
            }
            if(it.facesScores.isEmpty()) hits = max(hits - 1, 0)
            /* 当分数超过阈值，告警 */
            val now = System.currentTimeMillis()
            if(MonitorStats.riskScore >= AlarmSettings.alarmThreshold &&
                    now - MonitorManager.lastAlarm >= 60000){
                println("DEBUG: ${MonitorStats.riskScore}")
                println("DEBUG: ${MonitorStats.riskScoreStats}")
                BubbleAlarm.alarm()
                if(AlarmSettings.enablePopWindowAlarm) PopWindowAlarm.alarm()
                if(AlarmSettings.enableEmailAlarm) EmailAlarm.alarm()
                if(AlarmSettings.enableMessageAlarm) SMSAlarm.alarm()
                MonitorManager.lastAlarm = now
            }
        }, manual)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun doWork(): Result {
        hits = 0
        /* 切换为前台 Service */
        switchToForeground()
        delay(1000)
        /* 初始化截屏流 */
        val screenshot = MonitorManager.screenshot!!.also {
            it.startScreenShot()
        }
        if(AdvancedSettings.enableAuxiliaryDetection){
            KeywordDetector.init()
        }
        while (true){
            if(isStopped) break
            val riskLevel = RiskLevelManager.globalRiskLevel
            if(riskLevel == RiskLevel.NO_RISK){
                delay(RiskLevel.NO_RISK.detectionInterval)
                continue
            }
            if(riskLevel >= RiskLevel.HIGH_RISK){
                if (!KeywordDetector.isStart && AdvancedSettings.enableAuxiliaryDetection){
                    BubbleAlarm.keywordDetectorStartingAlarm()
                    KeywordDetector.start()
                }
            }else if(RiskLevelManager.sensitive){
                RiskLevelManager.sensitive = false
                KeywordDetector.stop()
            }
            screenshot.capture()?.let { bitmap ->
                if(hits < 5) {
                    FaceDetector.detectFaces(bitmap) { faces ->
                        if (faces.isNotEmpty()) {
                            hits = min(hits + 1, 10)
                            sendDetectionRequest(bitmap, false)
                        }
                    }
                }else{
                    sendDetectionRequest(bitmap, false)
                }
            }
            delay(riskLevel.detectionInterval)
        }
        return Result.success()
    }

}