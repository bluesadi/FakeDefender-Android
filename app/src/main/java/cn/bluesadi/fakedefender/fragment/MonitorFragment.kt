package cn.bluesadi.fakedefender.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.base.BaseHomeFragment
import cn.bluesadi.fakedefender.base.BaseTabFragment
import cn.bluesadi.fakedefender.core.MonitorManager
import cn.bluesadi.fakedefender.databinding.FragmentMonitorBinding
import com.google.android.material.tabs.TabLayout
import com.xuexiang.xpage.annotation.Page
import com.xuexiang.xui.adapter.FragmentAdapter
import com.xuexiang.xui.utils.WidgetUtils

@Page(name = "监测")
class MonitorFragment : BaseHomeFragment<FragmentMonitorBinding>() {

    private lateinit var tabMonitor : TabLayout
    private lateinit var viewPagerMonitor : ViewPager

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup
    ): FragmentMonitorBinding {
        return FragmentMonitorBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        binding!!.let {
            tabMonitor = it.tabMonitor
            viewPagerMonitor = it.viewPagerMonitor
        }
        viewPagerMonitor.apply {
            adapter = FragmentAdapter<BaseTabFragment>(childFragmentManager).apply {
                addFragment(MonitorMainFragment(), getString(R.string.monitor_main))
                addFragment(MonitorStatusFragment(), getString(R.string.monitor_status))
                addFragment(MonitorRecordFragment(), getString(R.string.monitor_record))
            }
        }
        tabMonitor.apply {
            tabMode = TabLayout.MODE_FIXED
            setupWithViewPager(viewPagerMonitor)
            WidgetUtils.setTabLayoutTextFont(this)
        }
    }

    override fun initListeners() {
        viewPagerMonitor.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) { }

            override fun onPageScrollStateChanged(state: Int) { }

            override fun onPageSelected(position: Int) {
                if (position == 1 && MonitorManager.needRefresh){
                    MonitorStatusFragment.INSTANCE.get()?.refresh()
                    MonitorManager.needRefresh = true
                }
            }
        })
    }


}