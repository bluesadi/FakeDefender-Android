package cn.bluesadi.fakedefender.data

import cn.bluesadi.fakedefender.R
import com.xuexiang.xutil.data.SPUtils

/**
 *
 * @author 34r7hm4n
 * @since 2022/3/14 14:00
 */
object Settings {

    private val appRiskLevelData = SPUtils.getSharedPreferences("app_risk_level_data")
    private val settings = SPUtils.getSharedPreferences("settings")

    fun getAppRiskLevelCode(packageName: String) : Int{
        return appRiskLevelData.getInt(packageName, 0)
    }

    fun getAppRiskLevel(packageName: String) : Pair<Int, Int>{
        return when(getAppRiskLevelCode(packageName)){
            3 -> Pair(R.string.high_risk, R.color.high_risk)
            2 -> Pair(R.string.medium_high_risk, R.color.medium_high_risk)
            1 -> Pair(R.string.medium_risk, R.color.medium_risk)
            else -> Pair(R.string.low_risk, R.color.low_risk)
        }
    }

    fun setAppRiskLevel(packageName: String, riskLevel: Int){
        appRiskLevelData.edit().apply {
            putInt(packageName, riskLevel)
        }.apply()
    }

    var highRiskThreshold
        get() = settings.getInt("high_risk_threshold", 90)
        set(value) = settings.edit().apply {
            putInt("high_risk_threshold", value)
        }.apply()

    var mediumRiskThreshold
        get() = settings.getInt("medium_risk_threshold", 80)
        set(value) = settings.edit().apply {
            putInt("medium_risk_threshold", value)
        }.apply()

    var mediumHighRiskThreshold
        get() = settings.getInt("medium_high_risk_threshold", 70)
        set(value) = settings.edit().apply {
            putInt("medium_high_risk_threshold", value)
        }.apply()

    var lowRiskThreshold
        get() = settings.getInt("low_risk_threshold", 60)
        set(value) = settings.edit().apply {
            putInt("low_risk_threshold", value)
        }.apply()

    var highScoreThreshold
        get() = settings.getInt("high_score_threshold", 70)
        set(value) = settings.edit().apply {
            putInt("high_score_threshold", value)
        }.apply()

    var mediumScoreThreshold
        get() = settings.getInt("medium_score_threshold", 70)
        set(value) = settings.edit().apply {
            putInt("medium_score_threshold", value)
        }.apply()

    var maxCheckRecordListSize
        get() = settings.getInt("max_check_record_list_size", 50)
        set(value) = settings.edit().apply {
            putInt("max_check_record_list_size", value)
        }.apply()
}