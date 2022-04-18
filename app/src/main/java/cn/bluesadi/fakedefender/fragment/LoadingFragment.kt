package cn.bluesadi.fakedefender.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import cn.bluesadi.fakedefender.FakeDefenderApp
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.activity.MainActivity
import cn.bluesadi.fakedefender.base.BaseFragment
import cn.bluesadi.fakedefender.databinding.FragmentLoadingBinding
import cn.bluesadi.fakedefender.databinding.FragmentLoginBinding
import cn.bluesadi.fakedefender.network.NetworkServices
import cn.bluesadi.fakedefender.util.ToastUtil
import cn.bluesadi.fakedefender.util.network.Http
import com.xuexiang.xpage.annotation.Page
import com.xuexiang.xpage.utils.TitleBar
import com.xuexiang.xui.utils.ViewUtils
import com.xuexiang.xui.widget.button.roundbutton.RoundButton
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText
import com.xuexiang.xui.widget.textview.supertextview.SuperButton
import com.xuexiang.xutil.app.ActivityUtils

@Page
class LoadingFragment : BaseFragment<FragmentLoadingBinding>() {

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup
    ): FragmentLoadingBinding {
        return FragmentLoadingBinding.inflate(inflater, container, false)
    }

    override fun initTitleBar(): TitleBar? {
        return null
    }

    override fun initViews() {

    }

    override fun initListeners() {

    }


}