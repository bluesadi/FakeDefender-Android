package cn.bluesadi.fakedefender.data

import cn.bluesadi.fakedefender.R
import com.xuexiang.xui.utils.ResUtils


/**
 *
 * @author 34r7hm4n
 * @since 2022/3/14 14:00
 */
object AdvancedSettings {

    var filterStrategy
        get() = advancedSettings.getInt("filter_strategy", 1)
        set(value) = advancedSettings.edit().apply {
            putInt("filter_strategy", value)
        }.apply()

    val filterStrategyDisplay
        get() = ResUtils.getString(when(filterStrategy){
            0 -> R.string.strict_filter_strategy
            1 -> R.string.moderate_filter_strategy
            else -> R.string.loose_filter_strategy
        })

    var enableAuxiliaryDetection
        get() = advancedSettings.getBoolean("enable_auxiliary_detection", false)
        set(value) = advancedSettings.edit().apply {
            putBoolean("enable_auxiliary_detection", value)
        }.apply()
}