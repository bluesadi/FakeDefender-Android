package cn.bluesadi.fakedefender.util

import android.app.ActivityManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.xuexiang.xui.XUI

/**
 *
 * @author 34r7hm4n
 * @since 2022/4/27 15:55
 */
object UStatsUtil {

    private val mUsageStatsManager by lazy {
        XUI.getContext().getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    }

    fun getRunningApp() : String?{
        val time = System.currentTimeMillis()
        val usageStatsList = mUsageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_BEST,
            time - 31536000000L,
            time
        ).apply {
            sortByDescending { it.lastTimeUsed }
        }
        return if(usageStatsList.isEmpty()) null else usageStatsList[0].packageName
    }

    fun checkPermission(context: Context){
        if (getRunningApp() == null){
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            context.startActivity(intent)
        }
    }

}