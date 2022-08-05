package cn.bluesadi.fakedefender.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.base.BaseActivity
import cn.bluesadi.fakedefender.databinding.ActivityMainBinding
import cn.bluesadi.fakedefender.fragment.MonitorFragment
import cn.bluesadi.fakedefender.fragment.SettingsFragment
import cn.bluesadi.fakedefender.fragment.UploadFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * 程序形式上的入口
 *
 * @author 34r7hm4n
 * @since 2022/3/4 19:44
 */
class MainActivity : BaseActivity<ActivityMainBinding?>() {

    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        switchPage(MonitorFragment::class.java)
        navView = binding!!.bottomNavigation
        navView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_monitor -> switchPage(MonitorFragment::class.java)
                R.id.nav_tools -> switchPage(UploadFragment::class.java)
                R.id.nav_settings -> switchPage(SettingsFragment::class.java)
            }
            true
        }
    }

    override fun viewBindingInflate(inflater: LayoutInflater?): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater!!)
    }

}