package cn.bluesadi.fakedefender.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.activity.LoginActivity
import cn.bluesadi.fakedefender.base.BaseTabFragment
import cn.bluesadi.fakedefender.core.risklevel.RiskLevel
import cn.bluesadi.fakedefender.data.AlarmSettings
import cn.bluesadi.fakedefender.data.Config
import cn.bluesadi.fakedefender.databinding.FragmentAlarmSettingsBinding
import cn.bluesadi.fakedefender.util.ToastUtil
import com.xuexiang.xui.utils.ResUtils
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet
import com.xuexiang.xui.widget.grouplist.XUICommonListItemView
import com.xuexiang.xui.widget.grouplist.XUIGroupListView
import com.xuexiang.xutil.app.ActivityUtils

/**
 *
 * @author 34r7hm4n
 * @since 2022/3/13 22:17
 */
class AlarmSettingsFragment : BaseTabFragment() {

    private lateinit var glvAlarmSettings : XUIGroupListView

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup
    ): FragmentAlarmSettingsBinding {
        return FragmentAlarmSettingsBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        (binding as? FragmentAlarmSettingsBinding)!!.let {
            glvAlarmSettings = it.glvAlarmSettings
        }
        val ivSetThreshold = glvAlarmSettings.createItemView(getString(R.string.set_alarm_threshold).format(
            AlarmSettings.alarmThreshold))
        XUIGroupListView.newSection(context)
            .setTitle(getString(R.string.alarm_threshold_settings))
            .addItemView(ivSetThreshold){
                BottomSheet.BottomListSheetBuilder(SceneSettingsFragment.INSTANCE?.get()?.activity)
                    .setTitle(R.string.please_select_threshold)
                    .addItem(ResUtils.getString(R.string.high_alarm_threshold))
                    .addItem(ResUtils.getString(R.string.medium_alarm_threshold))
                    .addItem(ResUtils.getString(R.string.low_alarm_threshold))
                    .setIsCenter(true)
                    .setOnSheetItemClickListener { dialog: BottomSheet, _: View?, threshold: Int, _: String? ->
                        dialog.dismiss()
                        when(threshold){
                            0 -> {
                                AlarmSettings.alarmThreshold = 90
                                ivSetThreshold.text = getString(R.string.set_alarm_threshold).format(90)
                            }
                            1 -> {
                                AlarmSettings.alarmThreshold = 80
                                ivSetThreshold.text = getString(R.string.set_alarm_threshold).format(80)
                            }
                            2 -> {
                                AlarmSettings.alarmThreshold = 70
                                ivSetThreshold.text = getString(R.string.set_alarm_threshold).format(70)
                            }
                        }
                    }
                    .build()
                    .show()
            }
            .addTo(glvAlarmSettings)

        val ivSetFrequency = glvAlarmSettings.createItemView(getString(R.string.set_detection_frequency).format(
            AlarmSettings.detectionFrequencyDisplay))
        val ivNoRiskFrequency = glvAlarmSettings.createItemView(getString(R.string.no_risk_frequency).format(
            RiskLevel.NO_RISK.detectionInterval))
        val ivLowRiskFrequency = glvAlarmSettings.createItemView(getString(R.string.low_risk_frequency).format(
            RiskLevel.LOW_RISK.detectionInterval))
        val ivMediumRiskFrequency = glvAlarmSettings.createItemView(getString(R.string.medium_risk_frequency).format(
            RiskLevel.MEDIUM_RISK.detectionInterval))
        val ivHighRiskFrequency = glvAlarmSettings.createItemView(getString(R.string.high_risk_frequency).format(
            RiskLevel.HIGH_RISK.detectionInterval))
        val ivExtremeHighFrequency = glvAlarmSettings.createItemView(getString(R.string.extreme_high_risk_frequency).format(
            RiskLevel.EXTREME_HIGH_RISK.detectionInterval))
        XUIGroupListView.newSection(context)
            .setTitle(getString(R.string.detection_frequency_settings))
            .addItemView(ivSetFrequency){
                BottomSheet.BottomListSheetBuilder(SceneSettingsFragment.INSTANCE?.get()?.activity)
                    .setTitle(R.string.please_select_frequency)
                    .addItem(ResUtils.getString(R.string.high_detection_frequency))
                    .addItem(ResUtils.getString(R.string.medium_detection_frequency))
                    .addItem(ResUtils.getString(R.string.low_detection_frequency))
                    .setIsCenter(true)
                    .setOnSheetItemClickListener { dialog: BottomSheet, _: View?, frequency: Int, _: String? ->
                        dialog.dismiss()
                        AlarmSettings.detectionFrequency = frequency
                        ivSetFrequency.text = getString(R.string.set_detection_frequency)
                            .format(AlarmSettings.detectionFrequencyDisplay)
                        ivNoRiskFrequency.text = getString(R.string.no_risk_frequency).format(
                            RiskLevel.NO_RISK.detectionInterval)
                        ivLowRiskFrequency.text = getString(R.string.low_risk_frequency).format(
                            RiskLevel.LOW_RISK.detectionInterval)
                        ivMediumRiskFrequency.text = getString(R.string.medium_risk_frequency).format(
                            RiskLevel.MEDIUM_RISK.detectionInterval)
                        ivHighRiskFrequency.text = getString(R.string.high_risk_frequency).format(
                            RiskLevel.HIGH_RISK.detectionInterval)
                        ivExtremeHighFrequency.text = getString(R.string.extreme_high_risk_frequency).format(
                            RiskLevel.EXTREME_HIGH_RISK.detectionInterval)
                    }
                    .build()
                    .show()
            }
            .addItemView(ivNoRiskFrequency) {}
            .addItemView(ivLowRiskFrequency) {}
            .addItemView(ivMediumRiskFrequency) {}
            .addItemView(ivHighRiskFrequency) {}
            .addItemView(ivExtremeHighFrequency) {}
            .addTo(glvAlarmSettings)

        XUIGroupListView.newSection(context)
            .setTitle(getString(R.string.alarm_threshold_settings))
            .addItemView(glvAlarmSettings.createItemView(getString(R.string.enable_bubble_alarm)).apply {
                accessoryType = XUICommonListItemView.ACCESSORY_TYPE_SWITCH
                switch.isChecked = AlarmSettings.enableBubbleAlarm
                switch.setOnCheckedChangeListener { _, isChecked ->
                    if(isChecked){

                    }else{

                    }
                }
            }){ }
            .addItemView(glvAlarmSettings.createItemView(getString(R.string.enable_email_alarm)).apply {
                accessoryType = XUICommonListItemView.ACCESSORY_TYPE_SWITCH
                switch.isChecked = AlarmSettings.enableEmailAlarm
                switch.setOnCheckedChangeListener { _, isChecked ->
                    if(isChecked){

                    }else{

                    }
                }
            }){ }
            .addItemView(glvAlarmSettings.createItemView(getString(R.string.enable_message_alarm)).apply {
                accessoryType = XUICommonListItemView.ACCESSORY_TYPE_SWITCH
                switch.isChecked = AlarmSettings.enableMessageAlarm
                switch.setOnCheckedChangeListener { _, isChecked ->
                    if(isChecked){

                    }else{

                    }
                }
            }){ }
            .addTo(glvAlarmSettings)
    }

}