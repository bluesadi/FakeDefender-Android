package cn.bluesadi.fakedefender.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.adapter.DetectionRecordListAdapter
import cn.bluesadi.fakedefender.base.BaseHomeFragment
import cn.bluesadi.fakedefender.base.BaseTabFragment
import cn.bluesadi.fakedefender.databinding.FragmentMonitorBinding
import cn.bluesadi.fakedefender.databinding.FragmentMonitorRecordBinding
import cn.bluesadi.fakedefender.databinding.FragmentMonitorStatisticBinding
import com.google.android.material.tabs.TabLayout
import com.xuexiang.xpage.annotation.Page
import com.xuexiang.xui.adapter.FragmentAdapter
import com.xuexiang.xui.utils.WidgetUtils

class MonitorStatisticFragment : BaseTabFragment() {


    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup
    ): FragmentMonitorStatisticBinding {
        return FragmentMonitorStatisticBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        (binding as? FragmentMonitorStatisticBinding)!!.let {

        }
    }

    override fun initListeners() {

    }

}