package cn.bluesadi.fakedefender.face

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import cn.bluesadi.fakedefender.adapter.DetectionRecordListAdapter
import cn.bluesadi.fakedefender.data.Config
import cn.bluesadi.fakedefender.fragment.MonitorMainFragment
import com.xuexiang.xui.XUI
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min

/**
 *
 * @author 34r7hm4n
 * @since 2022/4/8 15:43
 */
object MonitorManager {

    private const val INVOLVED_NUMBER = 10

    var riskScore = 0
    var running = false
    var runningSeconds = 0
    private val timer = Handler(Looper.getMainLooper())
    private val timerTask = object : Runnable{

        override fun run() {
            MonitorMainFragment.instance?.get()?.let {
                println(it)
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
        DetectionRecordListAdapter.INSTANCE.apply {
            addRecord(record, false)
            riskScore = data.subList(0, min(INVOLVED_NUMBER, data.size)).sumOf {
                100 / ln(101.0) * ln(it.riskScore + 1.0)
            }.toInt()
        }
    }

    fun startMonitor(){
        MonitorMainFragment.instance?.get()?.updateLastRunningTime()
        running = true
        timer.postAtTime(timerTask, SystemClock.uptimeMillis() + 1000)
        workManager.enqueueUniqueWork(MonitorWorker.UNIQUE_WORK_NAME, ExistingWorkPolicy.REPLACE, MonitorWorker.buildWorkRequest())
    }

    fun stopMonitor(){
        runningSeconds = 0
        running = false
        MonitorMainFragment.instance?.get()?.updateTimer(runningSeconds)
        timer.removeCallbacks(timerTask)
        workManager.cancelAllWork()
    }

}