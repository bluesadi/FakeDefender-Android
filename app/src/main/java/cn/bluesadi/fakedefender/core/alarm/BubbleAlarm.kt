package cn.bluesadi.fakedefender.core.alarm

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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

    fun alarm(){
        val notification = NotificationCompat.Builder(context, MonitorWorker.CHANNEL_ID)
            .setSmallIcon(cn.bluesadi.fakedefender.R.drawable.ic_logo)
            .setContentTitle(ResUtils.getString(cn.bluesadi.fakedefender.R.string.notification_alarm_title))
            .setContentText(ResUtils.getString(cn.bluesadi.fakedefender.R.string.notification_alarm_text))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        NotificationManagerCompat.from(context).notify(MonitorWorker.NOTIFICATION_ID, notification)
    }

}