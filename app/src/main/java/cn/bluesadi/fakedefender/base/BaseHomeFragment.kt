package cn.bluesadi.fakedefender.base

import android.view.View
import androidx.viewbinding.ViewBinding
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.network.NetworkServices
import cn.bluesadi.fakedefender.util.ToastUtil
import com.xuexiang.xpage.utils.TitleBar

/**
 *
 * @author 34r7hm4n
 * @since 2022/3/9 20:39
 */
abstract class BaseHomeFragment<Binding : ViewBinding?> : BaseFragment<Binding>() {

    override fun initTitleBar(): TitleBar {
        return super.initTitleBar().apply {
            setTitle(pageName)
            setLeftVisible(false)
            addAction(object : TitleBar.ImageAction(R.drawable.ic_action_about){
                override fun performAction(view: View?) {
                    NetworkServices.checkState {
                        println("DEBUG: " + it)
                    }
                }
            })
        }
    }


}