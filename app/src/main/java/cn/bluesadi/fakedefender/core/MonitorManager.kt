package cn.bluesadi.fakedefender.core

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.annotation.RequiresApi
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import cn.bluesadi.fakedefender.adapter.DetectionRecordListAdapter
import cn.bluesadi.fakedefender.core.alarm.PopWindowAlarm
import cn.bluesadi.fakedefender.core.risklevel.RiskLevelManager
import cn.bluesadi.fakedefender.fragment.MonitorMainFragment
import cn.bluesadi.fakedefender.util.media.Screenshot
import cn.bluesadi.fakedefender.util.UStatsUtil
import cn.bluesadi.fakedefender.util.media.AudioRecordUtil
import com.xuexiang.xui.XUI
import okio.ByteString.Companion.toByteString
import java.nio.ByteBuffer

/**
 *
 * @author 34r7hm4n
 * @since 2022/4/8 15:43
 */
object MonitorManager {

    private const val INVOLVED_NUMBER = 10

    var running = false
    var runningSeconds = 0
    var screenshot : Screenshot? = null
    var needRefresh = false
    var lastAlarm = 0L

    private val timer = Handler(Looper.getMainLooper())
    private val timerTask = object : Runnable{

        override fun run() {
            MonitorMainFragment.instance?.get()?.let {
                runningSeconds++
                it.updateTimer(runningSeconds)
                timer.postAtTime(this, SystemClock.uptimeMillis() + 1000)
            }
        }

    }

    private val workManager by lazy {
        WorkManager.getInstance(XUI.getContext())
    }

    fun update(record: DetectionRecord){
        MonitorStats.update(record)
        DetectionRecordListAdapter.INSTANCE.apply {
            addRecord(record, false)
        }
        needRefresh = true
    }



    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("UnsafeOptInUsageError")
    fun startMonitor(screenshot: Screenshot){
        MonitorStats.reset()
        lastAlarm = 0L
        RiskLevelManager.sensitive = false
        this.screenshot = screenshot
        MonitorMainFragment.instance?.get()?.apply{
            updateLastRunningTime()
            /* Check the permission for getting app stats */
            UStatsUtil.checkPermission(context!!)
        }
        running = true
        timer.postAtTime(timerTask, SystemClock.uptimeMillis() + 1000)
        workManager.enqueueUniqueWork(MonitorWorker.UNIQUE_WORK_NAME, ExistingWorkPolicy.REPLACE, MonitorWorker.buildWorkRequest())
    }

    fun stopMonitor(){
        screenshot?.destroy()
        screenshot = null
        runningSeconds = 0
        running = false
        MonitorMainFragment.instance?.get()?.updateTimer(runningSeconds)
        timer.removeCallbacks(timerTask)
        workManager.cancelAllWork()
    }

}