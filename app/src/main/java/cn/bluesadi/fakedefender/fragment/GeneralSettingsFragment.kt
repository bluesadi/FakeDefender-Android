package cn.bluesadi.fakedefender.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.bluesadi.fakedefender.base.BaseTabFragment
import cn.bluesadi.fakedefender.databinding.FragmentGeneralSettingsBinding
import cn.bluesadi.fakedefender.util.ToastUtil
import com.xuexiang.xui.widget.grouplist.XUIGroupListView
import com.xuexiang.xutil.app.ActivityUtils

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
        XUIGroupListView.newSection(context)
            .setDescription("TEST")
            .addItemView(glvGeneralSettings.createItemView("TEST")) { v: View? ->
                ToastUtil.info("Test")
            }
            .addTo(glvGeneralSettings)
    }

}