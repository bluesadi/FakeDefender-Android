package cn.bluesadi.fakedefender.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.base.BaseHomeFragment
import cn.bluesadi.fakedefender.base.BaseTabFragment
import cn.bluesadi.fakedefender.databinding.FragmentSettingsBinding
import com.google.android.material.tabs.TabLayout
import com.xuexiang.xpage.annotation.Page
import com.xuexiang.xui.adapter.FragmentAdapter
import com.xuexiang.xui.utils.WidgetUtils

@Page(name = "设置")
class SettingsFragment : BaseHomeFragment<FragmentSettingsBinding>() {

    private lateinit var tabSettings : TabLayout
    private lateinit var viewPagerSettings : ViewPager

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        binding!!.let {
            tabSettings = it.tabSettings
            viewPagerSettings = it.viewPagerSettings
        }
        viewPagerSettings.apply {
            adapter = FragmentAdapter<BaseTabFragment>(childFragmentManager).apply {
                addFragment(GeneralSettingsFragment(), getString(R.string.general_settings))
                addFragment(AlarmSettingsFragment(), getString(R.string.alarm_settings))
                addFragment(SceneSettingsFragment(), getString(R.string.scene_settings))
            }
        }
        tabSettings.apply {
//            tabMode = TabLayout.MODE_SCROLLABLE
            setupWithViewPager(viewPagerSettings)
            WidgetUtils.setTabLayoutTextFont(this)
        }
    }

    override fun initListeners() {

    }


}