package cn.bluesadi.fakedefender.core

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import cn.bluesadi.fakedefender.adapter.DetectionRecordListAdapter
import cn.bluesadi.fakedefender.core.risklevel.RiskLevelManager
import cn.bluesadi.fakedefender.fragment.MonitorMainFragment
import cn.bluesadi.fakedefender.fragment.MonitorStatusFragment
import cn.bluesadi.fakedefender.util.ImageUtil
import cn.bluesadi.fakedefender.util.Screenshot
import cn.bluesadi.fakedefender.util.UStatsUtil
import cn.bluesadi.fakedefender.util.d
import com.github.mikephil.charting.data.Entry
import com.xuexiang.xui.XUI
import kotlin.math.ln
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
    var screenshot : Screenshot? = null
    var refreshed = true

    var riskScoreRecords = mutableListOf<Entry>()
    var riskLevelRecords = mutableListOf<Entry>()
    var tenLatestRecords = mutableListOf<Entry>()

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
            val sum = data.subList(0, min(INVOLVED_NUMBER, data.size)).sumOf {
                it.riskScore
            }
            riskScore = ((-0.0001) * sum * sum + 0.2 * sum).toInt()
            riskScoreRecords.add(Entry(riskScoreRecords.size + 1f, riskScore.toFloat()))
            riskLevelRecords.add(Entry(riskLevelRecords.size + 1f, RiskLevelManager.globalRiskLevel.code.toFloat()))
            d(riskLevelRecords.toString())
            if(tenLatestRecords.size >= 10){
                tenLatestRecords.removeAt(0)
            }
            tenLatestRecords.add(Entry(0f, record.riskScore.toFloat()))
            repeat(min(10, tenLatestRecords.size)){
                tenLatestRecords[it].x = it + 1f
            }
            refreshed = false
        }
    }



    @SuppressLint("UnsafeOptInUsageError")
    fun startMonitor(screenshot: Screenshot){
        riskScore = 0
        riskScoreRecords = mutableListOf(Entry(1f, 0f))
        riskLevelRecords = mutableListOf()
        tenLatestRecords = mutableListOf()
        this.screenshot = screenshot
        MonitorMainFragment.instance?.get()?.apply{
            updateLastRunningTime()
            UStatsUtil.checkPermission(context!!)
        }
        /* Check the permission for getting app stats */
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