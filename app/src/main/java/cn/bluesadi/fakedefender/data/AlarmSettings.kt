package cn.bluesadi.fakedefender.data

import cn.bluesadi.fakedefender.R
import com.xuexiang.xui.utils.ResUtils

/**
 *
 * @author 34r7hm4n
 * @since 2022/5/5 16:34
 */
object AlarmSettings {

    var alarmThreshold
        get() = alarmSettings.getInt("alarm_threshold", 80)
        set(value) = alarmSettings.edit().apply {
            putInt("alarm_threshold", value)
        }.apply()

    var detectionFrequency
        get() = alarmSettings.getInt("detection_frequency", 1)
        set(value) = alarmSettings.edit().apply {
            putInt("detection_frequency", value)
        }.apply()

    val detectionFrequencyDisplay
        get() = when(detectionFrequency){
            0 -> ResUtils.getString(R.string.high_detection_frequency)
            1 -> ResUtils.getString(R.string.medium_detection_frequency)
            else -> ResUtils.getString(R.string.low_detection_frequency)
        }

    var enableBubbleAlarm
        get() = alarmSettings.getBoolean("enable_bubble_alarm", true)
        set(value) = alarmSettings.edit().apply {
            putBoolean("enable_bubble_alarm", value)
        }.apply()

    var enableEmailAlarm
        get() = alarmSettings.getBoolean("enable_email_alarm", false)
        set(value) = alarmSettings.edit().apply {
            putBoolean("enable_email_alarm", value)
        }.apply()

    var enableMessageAlarm
        get() = alarmSettings.getBoolean("enable_message_alarm", false)
        set(value) = alarmSettings.edit().apply {
            putBoolean("enable_message_alarm", value)
        }.apply()

}