package cn.bluesadi.fakedefender.data

import cn.bluesadi.fakedefender.R
import com.xuexiang.xui.utils.ResUtils


/**
 *
 * @author 34r7hm4n
 * @since 2022/3/14 14:00
 */
object GeneralSettings {

    var highScoreThreshold
        get() = settings.getInt("high_score_threshold", 70)
        set(value) = settings.edit().apply {
            putInt("high_score_threshold", value)
        }.apply()

    var mediumScoreThreshold
        get() = settings.getInt("medium_score_threshold", 50)
        set(value) = settings.edit().apply {
            putInt("medium_score_threshold", value)
        }.apply()

    var maxCheckRecordListSize
        get() = settings.getInt("max_check_record_list_size", 50)
        set(value) = settings.edit().apply {
            putInt("max_check_record_list_size", value)
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

    var enableAuxiliaryDetection
        get() = alarmSettings.getBoolean("enable_auxiliary_detection", false)
        set(value) = alarmSettings.edit().apply {
            putBoolean("enable_auxiliary_detection", value)
        }.apply()
}