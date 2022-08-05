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
import cn.bluesadi.fakedefender.databinding.FragmentAdvancedSettingsBinding
import cn.bluesadi.fakedefender.util.ImageUtil
import cn.bluesadi.fakedefender.util.ToastUtil
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
class AdvancedSettingsFragment : BaseTabFragment() {

    private lateinit var glvAdvancedSettings : XUIGroupListView

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup
    ): FragmentAdvancedSettingsBinding {
        return FragmentAdvancedSettingsBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        (binding as? FragmentAdvancedSettingsBinding)!!.let {
            glvAdvancedSettings = it.glvAdvancedSettings
        }
        /*
        * 启用语音辅助检测
        * */
        XUIGroupListView.newSection(context)
            .setTitle(getString(R.string.auxiliary_settings))
            .addItemView(glvAdvancedSettings.createItemView(getString(R.string.enable_auxiliary_detection)).apply {
                accessoryType = XUICommonListItemView.ACCESSORY_TYPE_SWITCH
                switch.isChecked = AdvancedSettings.enableAuxiliaryDetection
                switch.setOnCheckedChangeListener { _, isChecked ->
                    AdvancedSettings.enableAuxiliaryDetection = isChecked
                }
            }){ }
            .addTo(glvAdvancedSettings)
        /*
        * 模糊人脸过滤策略
        * */
        val ivSetFilterStrategy = glvAdvancedSettings.createItemView(getString(R.string.set_filter_strategy)
            .format(AdvancedSettings.filterStrategyDisplay))
        XUIGroupListView.newSection(context)
            .setTitle(getString(R.string.select_filter_strategy))
            .addItemView(ivSetFilterStrategy){
                BottomSheet.BottomListSheetBuilder(activity)
                    .setTitle(R.string.select_filter_strategy)
                    .addItem(ResUtils.getString(R.string.strict_filter_strategy))
                    .addItem(ResUtils.getString(R.string.moderate_filter_strategy))
                    .addItem(ResUtils.getString(R.string.loose_filter_strategy))
                    .setIsCenter(true)
                    .setOnSheetItemClickListener { dialog: BottomSheet, _: View?, strategy: Int, _: String? ->
                        dialog.dismiss()
                        AdvancedSettings.filterStrategy = strategy
                        ivSetFilterStrategy.text = getString(R.string.set_filter_strategy)
                            .format(AdvancedSettings.filterStrategyDisplay)
                    }
                    .build()
                    .show()
            }
            .setDescription(getString(R.string.description_filter_strategy))
            .addTo(glvAdvancedSettings)
    }

}