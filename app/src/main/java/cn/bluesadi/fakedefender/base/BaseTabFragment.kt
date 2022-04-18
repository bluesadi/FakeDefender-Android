package cn.bluesadi.fakedefender.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import cn.bluesadi.fakedefender.databinding.FragmentTabBinding
import com.xuexiang.xpage.annotation.Page
import com.xuexiang.xpage.utils.TitleBar

@Page
open class BaseTabFragment : BaseFragment<ViewBinding>() {

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup
    ): ViewBinding {
        return FragmentTabBinding.inflate(inflater, container, false)
    }

    override fun initTitleBar(): TitleBar? {
        return null
    }

    override fun initViews() {

    }

    override fun initListeners() {

    }


}