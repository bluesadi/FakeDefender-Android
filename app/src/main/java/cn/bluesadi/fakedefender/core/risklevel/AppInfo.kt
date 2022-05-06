package cn.bluesadi.fakedefender.core.risklevel

import android.graphics.drawable.Drawable
import cn.bluesadi.fakedefender.data.appRiskLevelData

/**
 *
 * @author 34r7hm4n
 * @since 2022/4/28 9:19
 */
class AppInfo(
    val packageName: String,
    val name: String,
    val icon: Drawable
){

    var riskLevel
        get() = RiskLevel.getFromCode(appRiskLevelData.getInt(packageName, 0))
        set(value) = appRiskLevelData.edit().apply {
            putInt(packageName, value.code)
        }.apply()

    val isRiskLevelSet: Boolean
        get() = appRiskLevelData.contains(packageName)

}
