package cn.bluesadi.fakedefender.adapter

import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.core.DetectionRecord
import cn.bluesadi.fakedefender.data.GeneralSettings
import cn.bluesadi.fakedefender.fragment.UploadFragment
import cn.bluesadi.fakedefender.util.TimeUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xuexiang.xui.utils.ResUtils

/**
 *
 * @author 34r7hm4n
 * @since 2022/3/25 16:58
 */
class DetectionRecordListAdapter(var title: TextView? = null) : BaseQuickAdapter<DetectionRecord, BaseViewHolder>(
    R.layout.item_detection_record, mutableListOf()) {

    companion object {
        val INSTANCE by lazy {
            DetectionRecordListAdapter()
        }
    }

    override fun convert(holder: BaseViewHolder, record: DetectionRecord) {
        holder.apply {
            setImageDrawable(R.id.im_record_summary, record.markedImage.toDrawable(ResUtils.getResources()))
            getView<TextView>(R.id.tx_record_summary).apply {
                text = ResUtils.getString(R.string.monitor_record_entry)
                    .format(record.riskScore, TimeUtil.formatTime(record.timestamp))
                setTextColor(record.riskColor)
                paint.isFakeBoldText = true
            }
            holder.itemView.setOnClickListener {
                UploadFragment.showDetectionResultDialog(context, record)
            }
        }
    }

    fun addRecord(record: DetectionRecord, manual: Boolean){
        if(data.size >= GeneralSettings.maxCheckRecordListSize){
            data.removeLast()
            notifyItemRemoved(data.size)
        }
        data.add(0, record)
        notifyItemInserted(0)
        title?.text = ResUtils.getString(R.string.monitor_record_amount).format(data.size)
    }

}