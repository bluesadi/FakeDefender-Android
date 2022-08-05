package cn.bluesadi.fakedefender.data

import android.content.SharedPreferences
import com.xuexiang.xutil.data.SPUtils

/**
 *
 * @author 34r7hm4n
 * @since 2022/4/28 8:59
 */
private fun getConfig(spName: String) : SharedPreferences{
    return SPUtils.getSharedPreferences("${spName}_${Config.phoneNumber}")
}

val appRiskLevelData by lazy {
    getConfig("app_risk_level_data")
}

val settings by lazy {
    getConfig("settings")
}

val alarmSettings by lazy {
    getConfig("alarm_settings")
}

val advancedSettings by lazy {
    getConfig("advanced_settings")
}

val config: SharedPreferences by lazy {
    SPUtils.getSharedPreferences("config")
}
