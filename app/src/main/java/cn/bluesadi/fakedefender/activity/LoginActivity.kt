package cn.bluesadi.fakedefender.activity

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import cn.bluesadi.fakedefender.base.BaseActivity
import cn.bluesadi.fakedefender.core.risklevel.RiskLevelManager
import cn.bluesadi.fakedefender.databinding.ActivityLoginBinding
import cn.bluesadi.fakedefender.fragment.LoadingFragment
import cn.bluesadi.fakedefender.fragment.LoginFragment
import cn.bluesadi.fakedefender.network.NetworkServices
import cn.bluesadi.fakedefender.util.network.Http
import com.xuexiang.xui.utils.StatusBarUtils
import com.xuexiang.xutil.app.ActivityUtils

/**
 *
 * @author 34r7hm4n
 * @since 2022/3/9 16:03
 */
class LoginActivity : BaseActivity<ActivityLoginBinding?>(){

    companion object{
        var login = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!login) {
            openPage(LoadingFragment::class.java)
            NetworkServices.checkState {
                RiskLevelManager.init()
                if (Http.validate(it)) {
                    login = true
                    ActivityUtils.startActivity(MainActivity::class.java)
                } else {
                    openPage(LoginFragment::class.java)
                }
            }
        }else{
            openPage(LoginFragment::class.java)
        }
    }

    override fun viewBindingInflate(inflater: LayoutInflater?): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(inflater!!)
    }

    override fun initStatusBarStyle(){
        StatusBarUtils.initStatusBarStyle(this, false, Color.TRANSPARENT)
    }

}