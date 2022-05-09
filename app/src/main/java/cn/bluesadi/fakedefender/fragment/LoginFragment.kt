package cn.bluesadi.fakedefender.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import cn.bluesadi.fakedefender.activity.LoginActivity
import cn.bluesadi.fakedefender.activity.MainActivity
import cn.bluesadi.fakedefender.base.BaseFragment
import cn.bluesadi.fakedefender.data.Config
import cn.bluesadi.fakedefender.databinding.FragmentLoginBinding
import cn.bluesadi.fakedefender.network.NetworkServices
import cn.bluesadi.fakedefender.util.ToastUtil
import com.xuexiang.xpage.annotation.Page
import com.xuexiang.xpage.utils.TitleBar
import com.xuexiang.xui.utils.CountDownButtonHelper
import com.xuexiang.xui.utils.ViewUtils
import com.xuexiang.xui.widget.button.roundbutton.RoundButton
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText
import com.xuexiang.xui.widget.textview.supertextview.SuperButton
import com.xuexiang.xutil.app.ActivityUtils

@Page
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private lateinit var etPhoneNumber: MaterialEditText
    private lateinit var etVerifyCode: MaterialEditText
    private lateinit var btnLogin: SuperButton
    private lateinit var btnGetVerifyCode: RoundButton
    private lateinit var cbProtocol: CheckBox
    private lateinit var mCountDownHelper: CountDownButtonHelper

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun initTitleBar(): TitleBar? {
        return null
    }

    override fun initViews() {
        binding?.let {
            etPhoneNumber = it.etPhoneNumber
            etVerifyCode = it.etVerifyCode
            btnLogin = it.btnLogin
            cbProtocol = it.cbProtocol
            btnGetVerifyCode = it.btnGetVerifyCode
        }
        ViewUtils.setEnabled(btnLogin, false)
        mCountDownHelper = CountDownButtonHelper(btnGetVerifyCode, 60)
    }

    override fun initListeners() {
        btnLogin.setOnClickListener {
            if(etPhoneNumber.validate() && etVerifyCode.validate()){
                NetworkServices.login(
                    etPhoneNumber.text.toString(),
                    etVerifyCode.text.toString())
                {
                    LoginActivity.login = true
                    Config.phoneNumber = etPhoneNumber.text.toString()
                    ToastUtil.success(it.getString("msg"))
                    ActivityUtils.startActivity(MainActivity::class.java)
                    popToBack()
                }
            }
        }
        btnGetVerifyCode.setOnClickListener {
            if(etPhoneNumber.validate()){
                mCountDownHelper.start()
                NetworkServices.getVerifyCode(etPhoneNumber.text.toString()){
                    ToastUtil.info(it.getString("msg"))
                }
            }
        }
        cbProtocol.setOnCheckedChangeListener { _, isChecked ->
            ViewUtils.setEnabled(btnLogin, isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mCountDownHelper.recycle()
    }


}