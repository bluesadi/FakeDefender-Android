package cn.bluesadi.fakedefender.base

import com.xuexiang.xpage.base.XPageActivity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.xuexiang.xpage.base.XPageFragment
import com.xuexiang.xpage.core.CoreSwitchBean
import io.github.inflationx.viewpump.ViewPumpContextWrapper

/**
 * 基础容器Activity
 *
 * @author XUE
 * @since 2019/3/22 11:21
 */
open class BaseActivity<Binding : ViewBinding?> : XPageActivity() {

    /**
     * ViewBinding
     */
    var binding: Binding? = null
        protected set

    override fun attachBaseContext(newBase: Context) {
        //注入字体
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun getCustomRootView(): View? {
        binding = viewBindingInflate(layoutInflater)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initStatusBarStyle()
        super.onCreate(savedInstanceState)
    }

    /**
     * 构建ViewBinding
     *
     * @param inflater  inflater
     * @return ViewBinding
     */
    protected open fun viewBindingInflate(inflater: LayoutInflater?): Binding? {
        return null
    }

    /**
     * 初始化状态栏的样式
     */
    protected open fun initStatusBarStyle() { }

    /**
     * 打开fragment
     *
     * @param clazz          页面类
     * @param addToBackStack 是否添加到栈中
     * @return 打开的fragment对象
     */
    fun <T : XPageFragment?> openPage(clazz: Class<T>?, addToBackStack: Boolean): T {
        val page = CoreSwitchBean(clazz)
            .setAddToBackStack(addToBackStack)
        return openPage(page) as T
    }

    /**
     * 打开fragment
     *
     * @return 打开的fragment对象
     */
    fun <T : XPageFragment?> openNewPage(clazz: Class<T>?): T {
        val page = CoreSwitchBean(clazz)
            .setNewActivity(true)
        return openPage(page) as T
    }

    /**
     * 切换fragment
     *
     * @param clazz 页面类
     * @return 打开的fragment对象
     */
    fun <T : XPageFragment?> switchPage(clazz: Class<T>?): T {
        return openPage(clazz, false)
    }

}