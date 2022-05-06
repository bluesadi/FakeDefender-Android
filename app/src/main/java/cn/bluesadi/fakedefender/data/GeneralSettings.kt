package cn.bluesadi.fakedefender.data


/**
 *
 * @author 34r7hm4n
 * @since 2022/3/14 14:00
 */
object GeneralSettings {

    fun getAppRiskLevelCode(packageName: String) : Int{
        return appRiskLevelData.getInt(packageName, 0)
    }

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
}