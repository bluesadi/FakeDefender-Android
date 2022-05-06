package cn.bluesadi.fakedefender.fragment

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bluesadi.fakedefender.adapter.AppListAdapter
import cn.bluesadi.fakedefender.base.BaseTabFragment
import cn.bluesadi.fakedefender.core.risklevel.RiskLevelManager
import cn.bluesadi.fakedefender.databinding.FragmentSceneSettingsBinding
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText
import java.lang.ref.WeakReference

/**
 *
 * @author 34r7hm4n
 * @since 2022/3/13 22:17
 */
class SceneSettingsFragment : BaseTabFragment() {

    private lateinit var rvAppList : RecyclerView
    private lateinit var etAppFilter : MaterialEditText

    companion object{
        var INSTANCE : WeakReference<SceneSettingsFragment>? = null
    }

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup
    ): FragmentSceneSettingsBinding {
        return FragmentSceneSettingsBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        INSTANCE = WeakReference(this)
        (binding as? FragmentSceneSettingsBinding)!!.let {
            rvAppList = it.rvAppList
            etAppFilter = it.etAppFilter
        }
        rvAppList.apply {
            adapter = AppListAdapter.INSTANCE
            layoutManager = LinearLayoutManager(activity)
        }
        etAppFilter.addTextChangedListener(object : TextWatcher{
            override fun onTextChanged(input: CharSequence, p1: Int, p2: Int, p3: Int) {
                (rvAppList.adapter as AppListAdapter).apply {
                    data = RiskLevelManager.sortedAppInfoList.filter { it.name.contains(input, true) }.toMutableList()
                    notifyDataSetChanged()
                }
            }

            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        })
    }

}