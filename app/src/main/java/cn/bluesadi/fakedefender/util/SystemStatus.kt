package cn.bluesadi.fakedefender.util

import android.content.pm.PackageManager
import android.net.TrafficStats
import cn.bluesadi.fakedefender.FakeDefenderApp
import com.xuexiang.xui.XUI

/**
 *
 * @author 34r7hm4n
 * @since 2022/8/4 16:52
 */
object SystemStatus {

    private val uid: Int by lazy {
        XUI.getContext().packageManager
            .getApplicationInfo(FakeDefenderApp.PACKAGE_NAME, PackageManager.GET_META_DATA).uid
    }

    private fun getRxBytes() : Long{
        return TrafficStats.getUidRxBytes(uid)
    }

    private fun getTxBytes() : Long{
        return TrafficStats.getUidTxBytes(uid)
    }

    private fun getTotalBytes() : Long{
        return getRxBytes() + getTxBytes()
    }

    fun getTotalTraffic() : Long{
        return getTotalBytes() ushr 20
    }

    fun getAllocatedMemory() : Long{
        return Runtime.getRuntime().totalMemory() ushr 20
    }

}