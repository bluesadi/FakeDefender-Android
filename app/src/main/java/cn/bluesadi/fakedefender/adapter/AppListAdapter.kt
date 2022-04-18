package cn.bluesadi.fakedefender.adapter

import android.widget.TextView
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.data.Settings
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xuexiang.xui.utils.ResUtils
import com.xuexiang.xutil.app.AppUtils

/**
 *
 * @author 34r7hm4n
 * @since 2022/3/13 22:46
 */
class AppListAdapter(data: MutableList<AppUtils.AppInfo>) : BaseQuickAdapter<AppUtils.AppInfo, BaseViewHolder>(R.layout.item_app_info, data) {

    override fun convert(holder: BaseViewHolder, appInfo: AppUtils.AppInfo) {
        holder.apply {
            val (text, color) = Settings.getAppRiskLevel(appInfo.packageName)
            setImageDrawable(R.id.im_app_icon, appInfo.icon)
            setText(R.id.tx_app_name, appInfo.name)
            setText(R.id.tx_app_risk_level, text)
            setTextColor(R.id.tx_app_risk_level, ResUtils.getColor(color))
            getView<TextView>(R.id.tx_app_name).paint.isFakeBoldText = true
        }
    }

}