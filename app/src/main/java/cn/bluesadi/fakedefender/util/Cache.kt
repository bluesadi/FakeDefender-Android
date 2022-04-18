package cn.bluesadi.fakedefender.util

import cn.bluesadi.fakedefender.data.Settings
import com.xuexiang.xutil.app.AppUtils
import java.util.concurrent.Executors

/**
 *
 * @author 34r7hm4n
 * @since 2022/3/14 16:16
 */
object Cache {

    var appInfoList: List<AppUtils.AppInfo> = listOf()

    fun init(){
        updateAppInfoList()
    }

    fun updateAppInfoList(){
        Executors.newSingleThreadExecutor().execute {
            appInfoList = AppUtils.getAppsInfo().filter { !it.isSystem }.toMutableList().apply {
                sortByDescending { info -> Settings.getAppRiskLevelCode(info.packageName) }
            }
        }
    }

}