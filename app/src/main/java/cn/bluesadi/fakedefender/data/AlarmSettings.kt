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

    var enablePopWindowAlarm
        get() = alarmSettings.getBoolean("enable_pop_window_alarm", false)
        set(value) = alarmSettings.edit().apply {
            putBoolean("enable_pop_window_alarm", value)
        }.apply()

    var enableEmailAlarm
        get() = alarmSettings.getBoolean("enable_email_alarm", false)
        set(value) = alarmSettings.edit().apply {
            putBoolean("enable_email_alarm", value)
        }.apply()

    var boundEmail
        get() = alarmSettings.getString("bound_email", null)
        set(value) = alarmSettings.edit().apply {
            putString("bound_email", value)
        }.apply()

    var enableMessageAlarm
        get() = alarmSettings.getBoolean("enable_message_alarm", false)
        set(value) = alarmSettings.edit().apply {
            putBoolean("enable_message_alarm", value)
        }.apply()

    var boundPhoneNumber
        get() = alarmSettings.getString("bound_phone_number", null)
        set(value) = alarmSettings.edit().apply {
            putString("bound_phone_number", value)
        }.apply()

}