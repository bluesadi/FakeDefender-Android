package cn.bluesadi.fakedefender.core.alarm

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cn.bluesadi.fakedefender.activity.MainActivity
import cn.bluesadi.fakedefender.core.MonitorWorker
import com.xuexiang.xui.XUI
import com.xuexiang.xui.utils.ResUtils

/**
 *
 * @author 34r7hm4n
 * @since 2022/5/5 8:58
 */
object BubbleAlarm {

    private val context
        get() = XUI.getContext()

    private fun alarm(title: Int, text: Int){
        alarm(
            ResUtils.getString(title),
            ResUtils.getString(text)
        )
    }

    private fun alarm(title: String, text: String){
        val intent = Intent(XUI.getContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(XUI.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(context, MonitorWorker.CHANNEL_ID)
            .setSmallIcon(cn.bluesadi.fakedefender.R.drawable.ic_logo)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        NotificationManagerCompat.from(context).notify(MonitorWorker.NOTIFICATION_ID, notification)
    }

    fun alarm(){
        alarm(
            cn.bluesadi.fakedefender.R.string.notification_alarm_title,
            cn.bluesadi.fakedefender.R.string.notification_alarm_text
        )
    }

    fun keywordDetectorStartingAlarm(){
        alarm(
            cn.bluesadi.fakedefender.R.string.keyword_detector_alarm_title,
            cn.bluesadi.fakedefender.R.string.keyword_detector_alarm_text
        )
    }

    fun sensitiveKeywordStartingAlarm(keyword: String){
        alarm(
            ResUtils.getString(cn.bluesadi.fakedefender.R.string.sensitive_keyword_alarm_title),
            ResUtils.getString(cn.bluesadi.fakedefender.R.string.sensitive_keyword_alarm_text).format(keyword)
        )
    }

}