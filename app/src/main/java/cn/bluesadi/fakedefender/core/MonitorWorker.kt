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
import cn.bluesadi.fakedefender.core.risklevel.RiskLevel
import cn.bluesadi.fakedefender.core.risklevel.RiskLevelManager
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

    private fun sendDetectionRequest(bitmap: Bitmap){
        NetworkServices.predict(bitmap) {
            d(it.riskScore.toString())
            handler.post {
                MonitorMainFragment.instance?.get()?.updateScore(MonitorManager.riskScore)
            }
            if(it.facesScores.isEmpty()) hits = max(hits - 1, 0)
            /* 当分数超过阈值，告警 */
            if(MonitorManager.riskScore >= AlarmSettings.alarmThreshold){
                if(AlarmSettings.enableBubbleAlarm) BubbleAlarm.alarm()
            }
        }
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
        while (true){
            if(isStopped) break
            val riskLevel = RiskLevelManager.globalRiskLevel
            if(riskLevel == RiskLevel.NO_RISK){
                delay(RiskLevel.NO_RISK.detectionInterval)
                continue
            }
            screenshot.capture()?.let { bitmap ->
                d("TEST")
                if(hits < 5) {
                    FaceDetector.detectFaces(bitmap) { faces ->
                        if (faces.isNotEmpty()) {
                            hits = min(hits + 1, 10)
                            sendDetectionRequest(bitmap)
                        }
                    }
                }else{
                    sendDetectionRequest(bitmap)
                }
            } ?: d("DEBUG: Fail")
            delay(riskLevel.detectionInterval)
        }
        return Result.success()
    }

}