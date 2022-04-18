package cn.bluesadi.fakedefender.fragment

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.adapter.AppListAdapter
import cn.bluesadi.fakedefender.base.BaseTabFragment
import cn.bluesadi.fakedefender.databinding.FragmentSceneSettingsBinding
import cn.bluesadi.fakedefender.util.Cache
import cn.bluesadi.fakedefender.data.Settings
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet.BottomListSheetBuilder
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText

/**
 *
 * @author 34r7hm4n
 * @since 2022/3/13 22:17
 */
class SceneSettingsFragment : BaseTabFragment() {

    private lateinit var rvAppList : RecyclerView
    private lateinit var etAppFilter : MaterialEditText

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup
    ): FragmentSceneSettingsBinding {
        return FragmentSceneSettingsBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        (binding as? FragmentSceneSettingsBinding)!!.let {
            rvAppList = it.rvAppList
            etAppFilter = it.etAppFilter
        }
        rvAppList.apply {
            adapter = AppListAdapter(Cache.appInfoList.toMutableList()).apply {
                setOnItemClickListener { _, _, itemPos ->
                    BottomListSheetBuilder(activity)
                        .setTitle(R.string.please_select_risk_level)
                        .addItem(getString(R.string.low_risk))
                        .addItem(getString(R.string.medium_risk))
                        .addItem(getString(R.string.medium_high_risk))
                        .addItem(getString(R.string.high_risk))
                        .setIsCenter(true)
                        .setOnSheetItemClickListener { dialog: BottomSheet, _: View?, riskLevel: Int, _: String? ->
                            dialog.dismiss()
                            Settings.setAppRiskLevel(data[itemPos].packageName , riskLevel)
                            val newPos = data.count { info ->
                                Settings.getAppRiskLevelCode(info.packageName) > riskLevel
                            }
                            val info = data[itemPos]
                            data.removeAt(itemPos)
                            data.add(newPos, info)
                            notifyItemChanged(itemPos)
                            notifyItemMoved(itemPos, newPos)
                        }
                        .build()
                        .show()
                }
            }
            layoutManager = LinearLayoutManager(activity)
        }
        etAppFilter.addTextChangedListener(object : TextWatcher{
            override fun onTextChanged(input: CharSequence, p1: Int, p2: Int, p3: Int) {
                (rvAppList.adapter as AppListAdapter).apply {
                    data = Cache.appInfoList.filter { it.name.contains(input, true) }.toMutableList()
                    notifyDataSetChanged()
                }
            }

            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        })
    }

}