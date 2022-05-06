package cn.bluesadi.fakedefender.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.activity.LoginActivity
import cn.bluesadi.fakedefender.adapter.AppListAdapter
import cn.bluesadi.fakedefender.base.BaseTabFragment
import cn.bluesadi.fakedefender.core.risklevel.RiskLevelManager
import cn.bluesadi.fakedefender.data.Config
import cn.bluesadi.fakedefender.databinding.FragmentGeneralSettingsBinding
import cn.bluesadi.fakedefender.util.ToastUtil
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
        XUIGroupListView.newSection(context)
            .setTitle(getString(R.string.account_settings))
            .addItemView(glvGeneralSettings.createItemView(getString(R.string.restore_default_settings))){
                runBlocking {
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