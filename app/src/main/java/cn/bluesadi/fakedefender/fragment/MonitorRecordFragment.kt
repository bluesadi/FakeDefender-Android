package cn.bluesadi.fakedefender.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.adapter.DetectionRecordListAdapter
import cn.bluesadi.fakedefender.base.BaseTabFragment
import cn.bluesadi.fakedefender.databinding.FragmentMonitorRecordBinding

class MonitorRecordFragment : BaseTabFragment() {

    private lateinit var txMonitorRecordAmount: TextView
    private lateinit var rvDetectionRecord: RecyclerView

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup
    ): FragmentMonitorRecordBinding {
        return FragmentMonitorRecordBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        (binding as? FragmentMonitorRecordBinding)!!.let {
            txMonitorRecordAmount = it.txMonitorRecordAmount
            rvDetectionRecord = it.rvDetectionRecord
        }
        rvDetectionRecord.adapter = DetectionRecordListAdapter.INSTANCE.apply {
            title = txMonitorRecordAmount.apply {
                text = getString(R.string.monitor_record_amount).format(data.size)
            }
        }
        rvDetectionRecord.layoutManager = LinearLayoutManager(activity)
    }

    override fun initListeners() {

    }


}