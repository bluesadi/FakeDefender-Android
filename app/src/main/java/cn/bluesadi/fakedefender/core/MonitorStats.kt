package cn.bluesadi.fakedefender.core

import cn.bluesadi.fakedefender.core.risklevel.RiskLevelManager
import cn.bluesadi.fakedefender.util.SystemStatus
import cn.bluesadi.fakedefender.util.d
import com.github.mikephil.charting.data.Entry
import java.lang.Integer.min

/**
 *
 * @author 34r7hm4n
 * @since 2022/8/4 17:22
 */
object MonitorStats {

    private var initialTraffic = 0L
    private val trafficRecords = mutableListOf<Long>()
    private val memoryRecords = mutableListOf<Long>()
    private val riskScoreRecords = mutableListOf<Long>()
    private val riskLevelRecords = mutableListOf<Long>()
    private val tenLatestRecords = mutableListOf<Long>()

    private const val MAX_STATS_SIZE = 100

    val trafficStats
        get() = toStats(trafficRecords, MAX_STATS_SIZE)

    val memoryStats
        get() = toStats(memoryRecords, MAX_STATS_SIZE)

    val riskScoreStats
        get() = toStats(riskScoreRecords, MAX_STATS_SIZE)

    val riskLevelStats
        get() = toStats(riskLevelRecords, MAX_STATS_SIZE)

    val tenLatestStats
        get() = toStats(tenLatestRecords, 10)

    var riskScore = 0

    private fun toStats(list: MutableList<Long>, maxSize: Int) : List<Entry>{
        return mutableListOf<Entry>().apply{
            repeat(min(list.size, maxSize)){ i ->
                add(Entry(i.toFloat() + 1f, list[i].toFloat()))
            }
        }
    }

    private fun MutableList<Long>.addRecord(ele: Long) {
        add(ele)
        if (size > MAX_STATS_SIZE) {
            removeAt((1 until MAX_STATS_SIZE - 1).random())
        }
    }

    fun reset(){
        initialTraffic = SystemStatus.getTotalTraffic()
        trafficRecords.clear()
        memoryRecords.clear()
        riskScoreRecords.clear()
        riskLevelRecords.clear()
        tenLatestRecords.clear()
        riskScore = 0
    }

    fun update(record: DetectionRecord){
        val sum = tenLatestRecords.sum()
        riskScore = ((-0.0001) * sum * sum + 0.2 * sum).toInt()
        trafficRecords.addRecord(SystemStatus.getTotalTraffic() - initialTraffic)
        memoryRecords.addRecord(SystemStatus.getAllocatedMemory())
        riskScoreRecords.addRecord(riskScore.toLong())
        riskLevelRecords.addRecord(RiskLevelManager.globalRiskLevel.code.toLong())
        if(tenLatestRecords.size >= 10){
           tenLatestRecords.removeAt(0)
        }
        tenLatestRecords.add(record.riskScore.toLong())
    }

}