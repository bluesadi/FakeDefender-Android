package cn.bluesadi.fakedefender.core.risklevel

import android.util.Log
import cn.bluesadi.fakedefender.FakeDefenderApp
import cn.bluesadi.fakedefender.adapter.AppListAdapter
import cn.bluesadi.fakedefender.data.appRiskLevelData
import cn.bluesadi.fakedefender.network.NetworkServices
import cn.bluesadi.fakedefender.util.UStatsUtil
import com.xuexiang.xutil.app.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 *
 * @author 34r7hm4n
 * @since 2022/4/24 20:46
 */
object RiskLevelManager {

    val globalRiskLevel: RiskLevel
        get() {
            var riskLevel = getAppInfo(UStatsUtil.getRunningApp() ?: "")?.riskLevel
                ?: RiskLevel.NO_RISK
            if (riskLevel == RiskLevel.HIGH_RISK && sensitive){
                riskLevel = RiskLevel.EXTREME_HIGH_RISK
            }
            return riskLevel
        }
    var sensitive = false

    private var appInfoList = mutableListOf<AppInfo>()
    val sortedAppInfoList: MutableList<AppInfo>
        get() {
            appInfoList.sortByDescending { info -> info.riskLevel }
            return appInfoList
        }

    private suspend fun getAppInfoList() : MutableList<AppInfo> = coroutineScope {
        withContext(Dispatchers.Default) {
            mutableListOf<AppInfo>().apply {
                AppUtils.getAppsInfo()
                    .filter { !it.isSystem && it.packageName != FakeDefenderApp.PACKAGE_NAME }
                    .forEach {
                    add(AppInfo(it.packageName, it.name, it.icon).apply {
                        if(!isRiskLevelSet){
                            NetworkServices.getAppCategory(packageName){ (categoryId, categoryName) ->
                                Log.d("DEBUG-RiskLevelManager", "$categoryId, $categoryName")
                                riskLevel = RiskLevel.getFromCategory(categoryName)
                            }
                        }
                    })
                }
                sortByDescending { info -> info.riskLevel }
            }
        }
    }

    fun init() = runBlocking{
        appInfoList = getAppInfoList()
    }

    fun getAppInfo(packageName: String) : AppInfo?{
        if(packageName == FakeDefenderApp.PACKAGE_NAME) return null
        appInfoList.forEach {
            if(it.packageName == packageName) return it
        }
        AppUtils.getAppInfo(packageName)?.let {
            return AppInfo(it.packageName, it.name, it.icon).apply {
                if(!isRiskLevelSet){
                    NetworkServices.getAppCategory(packageName){ (_, categoryName) ->
                        riskLevel = RiskLevel.getFromCategory(categoryName)
                    }
                }
            }
        }
        return null
    }

    fun reset(){
        appRiskLevelData.edit().clear().apply()
        appInfoList.forEach {
            if(!it.isRiskLevelSet){
                NetworkServices.getAppCategory(it.packageName){ (_, categoryName) ->
                    it.riskLevel = RiskLevel.getFromCategory(categoryName)
                    AppListAdapter.INSTANCE.notifyDataSetChanged()
                }
            }
        }
    }

}