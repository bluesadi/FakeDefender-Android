package cn.bluesadi.fakedefender.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.activity.LoginActivity
import cn.bluesadi.fakedefender.adapter.AppListAdapter
import cn.bluesadi.fakedefender.base.BaseTabFragment
import cn.bluesadi.fakedefender.core.DetectionRecord
import cn.bluesadi.fakedefender.core.risklevel.RiskLevel
import cn.bluesadi.fakedefender.core.risklevel.RiskLevelManager
import cn.bluesadi.fakedefender.data.*
import cn.bluesadi.fakedefender.databinding.FragmentGeneralSettingsBinding
import cn.bluesadi.fakedefender.util.ImageUtil
import cn.bluesadi.fakedefender.util.ToastUtil
import cn.bluesadi.fakedefender.util.setItemEnabled
import com.xuexiang.xui.utils.ResUtils
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import com.xuexiang.xui.widget.grouplist.XUICommonListItemView
import com.xuexiang.xui.widget.grouplist.XUIGroupListView
import com.xuexiang.xutil.app.ActivityUtils
import kotlinx.coroutines.runBlocking

/**
 *
 * @author 34r7hm4n
 * @since 2022/3/13 22:17
 */
class GeneralSettingsFragment : BaseTabFragment() {

    private lateinit var glvGeneralSettings : XUIGroupListView

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup
    ): FragmentGeneralSettingsBinding {
        return FragmentGeneralSettingsBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        (binding as? FragmentGeneralSettingsBinding)!!.let {
            glvGeneralSettings = it.glvGeneralSettings
        }
        val ivSetFrequency = glvGeneralSettings.createItemView(getString(R.string.set_detection_frequency).format(
            GeneralSettings.detectionFrequencyDisplay))
        val ivNoRiskFrequency = glvGeneralSettings.createItemView(getString(R.string.no_risk_frequency).format(
            RiskLevel.NO_RISK.detectionInterval)).apply { setItemEnabled(false) }
        val ivLowRiskFrequency = glvGeneralSettings.createItemView(getString(R.string.low_risk_frequency).format(
            RiskLevel.LOW_RISK.detectionInterval)).apply { setItemEnabled(false) }
        val ivMediumRiskFrequency = glvGeneralSettings.createItemView(getString(R.string.medium_risk_frequency).format(
            RiskLevel.MEDIUM_RISK.detectionInterval)).apply { setItemEnabled(false) }
        val ivHighRiskFrequency = glvGeneralSettings.createItemView(getString(R.string.high_risk_frequency).format(
            RiskLevel.HIGH_RISK.detectionInterval)).apply { setItemEnabled(false) }
        val ivExtremeHighFrequency = glvGeneralSettings.createItemView(getString(R.string.extreme_high_risk_frequency).format(
            RiskLevel.EXTREME_HIGH_RISK.detectionInterval)).apply { setItemEnabled(false) }
        XUIGroupListView.newSection(context)
            .setTitle(getString(R.string.detection_frequency_settings))
            .addItemView(ivSetFrequency){
                BottomSheet.BottomListSheetBuilder(activity)
                    .setTitle(R.string.please_select_frequency)
                    .addItem(ResUtils.getString(R.string.high_detection_frequency))
                    .addItem(ResUtils.getString(R.string.medium_detection_frequency))
                    .addItem(ResUtils.getString(R.string.low_detection_frequency))
                    .setIsCenter(true)
                    .setOnSheetItemClickListener { dialog: BottomSheet, _: View?, frequency: Int, _: String? ->
                        dialog.dismiss()
                        GeneralSettings.detectionFrequency = frequency
                        ivSetFrequency.text = getString(R.string.set_detection_frequency)
                            .format(GeneralSettings.detectionFrequencyDisplay)
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
            .addTo(glvGeneralSettings)

        /*
        * 图像质量设置
        * */
        val ivSetImageQuality = glvGeneralSettings.createItemView(getString(R.string.set_image_quality)
            .format(GeneralSettings.imageQualityDisplay))
        XUIGroupListView.newSection(context)
            .setTitle(getString(R.string.image_quality_settings))
            .setDescription(getString(R.string.set_image_quality_desc))
            .addItemView(ivSetImageQuality){
                BottomSheet.BottomListSheetBuilder(activity)
                    .setTitle(R.string.select_image_quality)
                    .addItem(ResUtils.getString(R.string.high_image_quality))
                    .addItem(ResUtils.getString(R.string.moderate_image_quality))
                    .addItem(ResUtils.getString(R.string.low_image_quality))
                    .setIsCenter(true)
                    .setOnSheetItemClickListener { dialog: BottomSheet, _: View?, quality: Int, _: String? ->
                        dialog.dismiss()
                        GeneralSettings.imageQuality = quality
                        ivSetImageQuality.text = getString(R.string.set_image_quality)
                            .format(GeneralSettings.imageQualityDisplay)
                    }
                    .build()
                    .show()
            }
            .addTo(glvGeneralSettings)

        XUIGroupListView.newSection(context)
            .setTitle(getString(R.string.account_settings))
            .addItemView(glvGeneralSettings.createItemView(getString(R.string.restore_default_settings))){
                runBlocking {
                    settings.edit().clear().apply()
                    alarmSettings.edit().clear().apply()
                    advancedSettings.edit().clear().apply()
                    RiskLevelManager.reset()
                    AppListAdapter.INSTANCE.notifyDataSetChanged()
                    ToastUtil.info(getString(R.string.restore_default_settings_completed))
                }
            }
            .addItemView(glvGeneralSettings.createItemView(getString(R.string.logout).format(Config.phoneNumber))){
                Config.cookie = null
                Config.phoneNumber = null
                ActivityUtils.startActivity(LoginActivity::class.java)
                ToastUtil.info(getString(R.string.info_logout))
            }
            .addTo(glvGeneralSettings)
    }

}