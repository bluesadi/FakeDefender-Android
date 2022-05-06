package cn.bluesadi.fakedefender.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import cn.bluesadi.fakedefender.base.BaseTabFragment
import cn.bluesadi.fakedefender.databinding.FragmentMonitorMainBinding
import cn.bluesadi.fakedefender.core.MonitorManager
import com.xuexiang.xui.widget.button.roundbutton.RoundButton
import java.lang.ref.WeakReference
import android.view.animation.AccelerateDecelerateInterpolator
import android.animation.ValueAnimator
import android.app.Activity
import android.widget.ImageView
import cn.bluesadi.fakedefender.data.Config
import cn.bluesadi.fakedefender.util.TimeUtil
import android.content.Intent
import android.app.Activity.RESULT_OK
import android.content.Context
import android.media.projection.MediaProjectionManager
import androidx.activity.result.contract.ActivityResultContracts
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.util.Screenshot
import cn.bluesadi.fakedefender.util.ToastUtil


class MonitorMainFragment : BaseTabFragment() {

    companion object{
        var instance : WeakReference<MonitorMainFragment>? = null
    }

    private lateinit var btnStartMonitor: RoundButton
    private lateinit var btnStopMonitor: RoundButton
    private lateinit var txScore: TextView
    private lateinit var txMonitorHour: TextView
    private lateinit var txMonitorMinute: TextView
    private lateinit var txMonitorSecond: TextView
    private lateinit var layoutLogo: LinearLayout
    private lateinit var ivLogo: ImageView
    private lateinit var layoutMonitorPanel: LinearLayout
    private lateinit var txRunningTime: TextView
    private lateinit var txLastRunningTime: TextView

    @SuppressLint("SetTextI18n")
    fun updateTimer(runSeconds: Int){
        val hours = runSeconds / 3600
        val minutes = (runSeconds - 3600 * hours) / 60
        val seconds = runSeconds % 60
        txMonitorHour.text = "%02d".format(hours)
        txMonitorMinute.text = "%02d".format(minutes)
        txMonitorSecond.text = "%02d".format(seconds)
    }

    fun updateScore(score: Int){
        txScore.text = "%d".format(score)
    }

    fun updateLastRunningTime(){
        val now = System.currentTimeMillis()
        Config.lastRunningTime = now
        txLastRunningTime.text = getString(R.string.last_running_time).format(TimeUtil.formatTime(now))
    }

    private fun setTranslateAnimation(view: ImageView) {
        val animator = ValueAnimator.ofInt(0, -30, 0)
        animator.addUpdateListener { animation ->
            view.translationY = (animation.animatedValue as Int).toFloat()
            view.requestLayout()
        }
        animator.repeatMode = ValueAnimator.RESTART
        animator.repeatCount = ValueAnimator.INFINITE
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = 2000
        animator.start()
    }

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup
    ): FragmentMonitorMainBinding {
        return FragmentMonitorMainBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        (binding as? FragmentMonitorMainBinding)!!.let {
            btnStartMonitor = it.btnStartMonitor
            btnStopMonitor = it.btnStopMonitor
            txScore = it.txScore
            txMonitorHour = it.txMonitorHour
            txMonitorMinute = it.txMonitorMinute
            txMonitorSecond = it.txMonitorSecond
            layoutLogo = it.layoutLogo
            ivLogo = it.ivLogo
            layoutMonitorPanel = it.layoutMonitorPanel
            txRunningTime = it.txRunningTime
            txLastRunningTime = it.txLastRunningTime
        }
        txRunningTime.text = getString(R.string.running_time).format(TimeUtil.getDaysFrom(Config.firstRunningTime))
        txLastRunningTime.text = Config.lastRunningTime.takeIf { it > 0 }?.let {
            getString(R.string.last_running_time).format(TimeUtil.formatTime(it))
        } ?: getString(R.string.not_run_yet)
        setTranslateAnimation(ivLogo)
        instance = WeakReference(this)
        setRunningMode(MonitorManager.running)
        updateTimer(MonitorManager.runningSeconds)
    }

    private fun setRunningMode(running: Boolean){
        if(running){
            layoutLogo.visibility = View.GONE
            layoutMonitorPanel.visibility = View.VISIBLE
            btnStartMonitor.visibility = View.GONE
            btnStopMonitor.visibility = View.VISIBLE
        }else{
            layoutLogo.visibility = View.VISIBLE
            layoutMonitorPanel.visibility = View.GONE
            btnStartMonitor.visibility = View.VISIBLE
            btnStopMonitor.visibility = View.GONE
        }
    }

    override fun initListeners() {
        btnStartMonitor.setOnClickListener {
            launchMonitor()
        }
        btnStopMonitor.setOnClickListener {
            setRunningMode(false)
            MonitorManager.stopMonitor()
        }
    }


    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            data?.let {
                setRunningMode(true)
                MonitorManager.startMonitor(Screenshot(data))
            }
        }
    }

    /**
     * 动态请求屏幕截图的权限
     * 并启动全流程监测
     */
    fun launchMonitor() {
        val mediaProjectionManager =
            context!!.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        resultLauncher.launch(mediaProjectionManager.createScreenCaptureIntent())
    }

}