package cn.bluesadi.fakedefender.adapter

import android.view.View
import android.widget.TextView
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.core.risklevel.AppInfo
import cn.bluesadi.fakedefender.core.risklevel.RiskLevel
import cn.bluesadi.fakedefender.core.risklevel.RiskLevelManager
import cn.bluesadi.fakedefender.data.GeneralSettings
import cn.bluesadi.fakedefender.fragment.SceneSettingsFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xuexiang.xui.XUI
import com.xuexiang.xui.utils.ResUtils
import com.xuexiang.xui.utils.ResUtils.getString
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet
import com.xuexiang.xutil.app.AppUtils

/**
 *
 * @author 34r7hm4n
 * @since 2022/3/13 22:46
 */
class AppListAdapter(data: MutableList<AppInfo>) : BaseQuickAdapter<AppInfo, BaseViewHolder>(R.layout.item_app_info, data) {

    companion object {
        val INSTANCE by lazy {
            AppListAdapter(RiskLevelManager.sortedAppInfoList).apply {
                setOnItemClickListener { _, _, itemPos ->
                    BottomSheet.BottomListSheetBuilder(SceneSettingsFragment.INSTANCE?.get()?.activity)
                        .setTitle(R.string.please_select_risk_level)
                        .addItem(getString(R.string.no_risk))
                        .addItem(getString(R.string.low_risk))
                        .addItem(getString(R.string.medium_risk))
                        .addItem(getString(R.string.high_risk))
                        .setIsCenter(true)
                        .setOnSheetItemClickListener { dialog: BottomSheet, _: View?, riskLevel: Int, _: String? ->
                            dialog.dismiss()
                            RiskLevelManager.getAppInfo(data[itemPos].packageName)?.let {
                                it.riskLevel = RiskLevel.getFromCode(riskLevel)
                                val newPos = data.count { info ->
                                    info.riskLevel.code > it.riskLevel.code
                                }
                                val info = data[itemPos]
                                data.removeAt(itemPos)
                                data.add(newPos, info)
                                notifyItemChanged(itemPos)
                                notifyItemMoved(itemPos, newPos)
                            }
                        }
                        .build()
                        .show()
                }
            }
        }
    }

    override fun convert(holder: BaseViewHolder, appInfo: AppInfo) {
        holder.apply {
            setImageDrawable(R.id.im_app_icon, appInfo.icon)
            setText(R.id.tx_app_name, appInfo.name)
            setText(R.id.tx_app_risk_level, appInfo.riskLevel.display)
            setTextColor(R.id.tx_app_risk_level, appInfo.riskLevel.color)
            getView<TextView>(R.id.tx_app_name).paint.isFakeBoldText = true
        }
    }

}