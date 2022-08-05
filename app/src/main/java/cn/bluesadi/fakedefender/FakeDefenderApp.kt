package cn.bluesadi.fakedefender

import android.app.Application
import cn.bluesadi.fakedefender.base.BaseActivity
import com.xuexiang.xui.XUI
import com.xuexiang.xpage.PageConfig
import com.xuexiang.xutil.XUtil
import com.xuexiang.xutil.data.SPUtils

class FakeDefenderApp : Application() {

    override fun onCreate() {
        super.onCreate()
        XUI.init(this)
        XUI.debug(true)
        XUtil.init(this)
        PageConfig.getInstance()
            .debug("PageLog") //开启调试
            .setContainActivityClazz(BaseActivity::class.java) //设置默认的容器Activity
            .init(this) //初始化页面配置
    }

    companion object {
        const val DEBUG = true

        const val PACKAGE_NAME = "cn.bluesadi.fakedefender"
    }


}