package cn.bluesadi.fakedefender.core.alarm

import android.app.AlertDialog
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.activity.MainActivity
import cn.bluesadi.fakedefender.core.MonitorStats
import cn.bluesadi.fakedefender.data.AlarmSettings
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.xuexiang.xui.XUI
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import com.xuexiang.xutil.app.ActivityUtils
import com.xuexiang.xutil.common.StringUtils
import com.xuexiang.xutil.resource.ResUtils
import com.xuexiang.xutil.resource.ResourceUtils

/**
 *
 * @author 34r7hm4n
 * @since 2022/8/5 11:37
 */
object PopWindowAlarm {

    fun alarm(){
        Handler(Looper.getMainLooper()).post {
            AlertDialog.Builder(XUI.getContext()).apply {
                setTitle(R.string.pop_window_alarm_title)
                setIcon(R.drawable.ic_warn)
                setCancelable(false)
                setPositiveButton(R.string.switch_back_to_app){ dialog, _ ->
                    dialog.dismiss()
                    ActivityUtils.startActivity(MainActivity::class.java)
                }
                setNegativeButton(R.string.ignore_pop_window_alarm){ dialog, _ ->
                    dialog.dismiss()
                }
            }.create().apply {
                setView(View.inflate(context, R.layout.dialog_pop_window_alarm, listView).apply {
                    findViewById<LineChart>(R.id.ct_scores_in_dialog)?.apply {
                        legend.isEnabled = false
                        description.isEnabled = false
                        isDragEnabled = false
                        setScaleEnabled(false)
                        xAxis.isEnabled = false
                        axisLeft.apply {
                            axisRight.isEnabled = false
                            enableGridDashedLine(10f, 10f, 0f)
                            axisMaximum = 100f
                            axisMinimum = 0f
                            addLimitLine(LimitLine(AlarmSettings.alarmThreshold.toFloat(), ResUtils.getString(R.string.alarm_threshold)).apply {
                                lineWidth = 2f;
                                enableDashedLine(10f, 10f, 0f);
                                labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM;
                                textSize = 10f;
                            })
                        }
                        data = LineData(LineDataSet(MonitorStats.riskScoreStats, null).apply {
                            fillColor = Color.RED
                            color = Color.BLACK
                            setDrawFilled(true)
                            setDrawValues(false)
                            setDrawCircles(false)
                        })
                        invalidate()
                    }
                })
                window?.apply {
                    setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
                    setBackgroundDrawableResource(R.drawable.bg_dialog_common_tip_corner_white)
                }
                show()
            }
        }
    }

    fun checkPermission(fragment: Fragment, callback: ActivityResultCallback<Boolean>){
        if(!Settings.canDrawOverlays(fragment.context)){
            fragment.registerForActivityResult(ActivityResultContracts.RequestPermission(), callback)
                .launch(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        }else{
            callback.onActivityResult(true)
        }
    }

}