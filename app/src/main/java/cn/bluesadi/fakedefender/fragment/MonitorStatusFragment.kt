package cn.bluesadi.fakedefender.fragment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.size
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.base.BaseTabFragment
import cn.bluesadi.fakedefender.core.MonitorManager
import cn.bluesadi.fakedefender.core.risklevel.RiskLevel
import cn.bluesadi.fakedefender.data.AlarmSettings
import cn.bluesadi.fakedefender.databinding.FragmentMonitorStatusBinding
import cn.bluesadi.fakedefender.util.d
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.lang.ref.WeakReference
import kotlin.math.min

class MonitorStatusFragment : BaseTabFragment() {

    private lateinit var ctScores: LineChart
    private lateinit var ctRecords: LineChart
    private lateinit var ctRiskLevels: LineChart

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup
    ): FragmentMonitorStatusBinding {
        return FragmentMonitorStatusBinding.inflate(inflater, container, false)
    }

    companion object{
        lateinit var INSTANCE : WeakReference<MonitorStatusFragment>
    }

    fun refresh(){
        ctScores.apply {
            data = LineData(LineDataSet(MonitorManager.riskScoreRecords, null).apply {
                fillColor = Color.RED
                color = Color.BLACK
                setDrawFilled(true)
                setDrawValues(false)
                setDrawCircles(false)
            })
            invalidate()
        }
        ctRecords.apply {
            data = LineData(LineDataSet(MonitorManager.tenLatestRecords, null).apply {
                //fillDrawable = ContextCompat.getDrawable(context, R.drawable.fade_red)
                fillColor = Color.RED
                setDrawFilled(true)
                color = Color.BLACK
                setCircleColor(Color.BLACK)
            })
            invalidate()
        }
        ctRiskLevels.apply {
            data = LineData(LineDataSet(MonitorManager.riskLevelRecords, null).apply {
                //fillDrawable = ContextCompat.getDrawable(context, R.drawable.fade_red)
                fillColor = Color.RED
                color = Color.BLACK
                setDrawFilled(true)
                setDrawValues(false)
                setDrawCircles(false)
            })
            invalidate()
        }
    }

    override fun initViews() {
        INSTANCE = WeakReference(this)
        (binding as? FragmentMonitorStatusBinding)!!.let {
            ctScores = it.ctScores
            ctRecords = it.ctRecords
            ctRiskLevels = it.ctRiskLevels
        }

        ctScores.apply {
            legend.isEnabled = false
            description.isEnabled = false
            isDragEnabled = false
            setScaleEnabled(false)
            xAxis.apply {
                enableGridDashedLine(10f, 10f, 0f)
                granularity = 1f
                axisMinimum = 1f
                position = XAxis.XAxisPosition.BOTTOM
            }
            axisLeft.apply {
                axisRight.isEnabled = false
                enableGridDashedLine(10f, 10f, 0f)
                axisMaximum = 100f
                axisMinimum = 0f
                addLimitLine(LimitLine(AlarmSettings.alarmThreshold.toFloat(), getString(R.string.alarm_threshold)).apply {
                    lineWidth = 2f;
                    enableDashedLine(10f, 10f, 0f);
                    labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM;
                    textSize = 10f;
                })
            }
        }

        ctRecords.apply {
            legend.isEnabled = false
            description.isEnabled = false
            isDragEnabled = false
            setScaleEnabled(false)
            xAxis.apply {
                enableGridDashedLine(10f, 10f, 0f)
                granularity = 1f
                axisMaximum = 10f
                axisMinimum = 1f
                position = XAxis.XAxisPosition.BOTTOM
            }
            axisLeft.apply {
                axisRight.isEnabled = false
                enableGridDashedLine(10f, 10f, 0f)
                axisMaximum = 100f
                axisMinimum = 0f
            }

        }

        ctRiskLevels.apply {
            legend.isEnabled = false
            description.isEnabled = false
            isDragEnabled = false
            setScaleEnabled(false)
            xAxis.apply {
                enableGridDashedLine(10f, 10f, 0f)
                granularity = 1f
                axisMinimum = 1f
                position = XAxis.XAxisPosition.BOTTOM
            }
            axisLeft.apply {
                granularity = 1f
                axisRight.isEnabled = false
                enableGridDashedLine(10f, 10f, 0f)
                axisMaximum = 4f
                axisMinimum = 0f
                valueFormatter = IndexAxisValueFormatter(listOf(
                    getString(R.string.no_risk),
                    getString(R.string.low_risk),
                    getString(R.string.medium_risk),
                    getString(R.string.high_risk),
                    getString(R.string.extreme_high_risk)
                ))
            }
        }
        refresh()
    }

    override fun initListeners() {

    }

}