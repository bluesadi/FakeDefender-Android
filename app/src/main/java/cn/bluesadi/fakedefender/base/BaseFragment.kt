package cn.bluesadi.fakedefender.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.xuexiang.xpage.base.XPageFragment

/**
 * @author 34r7hm4n
 * @since 2022/3/7 14:06
 *
 * Fragment基类
 */
abstract class BaseFragment<Binding : ViewBinding?> : XPageFragment() {

    var binding: Binding? = null
        protected set

    /**
     * 构建ViewBinding
     *
     * @param inflater  inflater
     * @param container 容器
     * @return ViewBinding
     */
    protected abstract fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup
    ): Binding

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        binding = viewBindingInflate(inflater, container)
        return binding!!.root
    }

}