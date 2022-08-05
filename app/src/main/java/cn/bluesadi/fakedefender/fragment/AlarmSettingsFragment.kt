package cn.bluesadi.fakedefender.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.base.BaseTabFragment
import cn.bluesadi.fakedefender.core.alarm.PopWindowAlarm
import cn.bluesadi.fakedefender.data.AlarmSettings
import cn.bluesadi.fakedefender.databinding.FragmentAlarmSettingsBinding
import cn.bluesadi.fakedefender.util.ToastUtil
import cn.bluesadi.fakedefender.util.setItemEnabled
import com.xuexiang.xui.utils.ResUtils
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText
import com.xuexiang.xui.widget.grouplist.XUICommonListItemView
import com.xuexiang.xui.widget.grouplist.XUIGroupListView

/**
 *
 * @author 34r7hm4n
 * @since 2022/3/13 22:17
 */
class AlarmSettingsFragment : BaseTabFragment() {

    private lateinit var glvAlarmSettings : XUIGroupListView

    companion object {
        fun showEditBoundEmailDialog(context: Context, ivBoundEmail: XUICommonListItemView){
            val dialog: MaterialDialog = MaterialDialog.Builder(context)
                .customView(R.layout.dialog_edit_email, true)
                .title("修改绑定邮箱")
                .build()
            val etBoundEmail = dialog.findViewById<MaterialEditText>(R.id.et_bound_email)
            AlarmSettings.boundEmail?.let {
                etBoundEmail.setText(it)
            }
            val btnSaveSetting = dialog.findViewById<Button>(R.id.btn_save_email)
            btnSaveSetting.setOnClickListener {
                if(etBoundEmail.validate()){
                    ivBoundEmail.text = ResUtils.getString(R.string.bound_email)
                        .format(etBoundEmail.text.toString())
                    AlarmSettings.boundEmail = etBoundEmail.text.toString()
                    dialog.dismiss()
                }
            }
            dialog.show()
        }

        fun showEditBoundPhoneNumberDialog(context: Context, ivBoundPhoneNumber: XUICommonListItemView){
            val dialog: MaterialDialog = MaterialDialog.Builder(context)
                .customView(R.layout.dialog_edit_phone_number, true)
                .title("修改绑定手机号")
                .build()
            val etBoundPhoneNumber = dialog.findViewById<MaterialEditText>(R.id.et_bound_phone_number)
            AlarmSettings.boundPhoneNumber?.let {
                etBoundPhoneNumber.setText(it)
            }
            val btnSaveSetting = dialog.findViewById<Button>(R.id.btn_save_phone_number)
            btnSaveSetting.setOnClickListener {
                if(etBoundPhoneNumber.validate()) {
                    ivBoundPhoneNumber.text = ResUtils.getString(R.string.bound_phone_number)
                        .format(etBoundPhoneNumber.text.toString())
                    AlarmSettings.boundPhoneNumber = etBoundPhoneNumber.text.toString()
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
    }


    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup
    ): FragmentAlarmSettingsBinding {
        return FragmentAlarmSettingsBinding.inflate(inflater, container, false)
    }



    override fun initViews() {
        (binding as? FragmentAlarmSettingsBinding)!!.let {
            glvAlarmSettings = it.glvAlarmSettings
        }
        val ivSetThreshold = glvAlarmSettings.createItemView(getString(R.string.set_alarm_threshold).format(
            AlarmSettings.alarmThreshold))
        XUIGroupListView.newSection(context)
            .setTitle(getString(R.string.alarm_threshold_settings))
            .addItemView(ivSetThreshold){
                BottomSheet.BottomListSheetBuilder(SceneSettingsFragment.INSTANCE?.get()?.activity)
                    .setTitle(R.string.please_select_threshold)
                    .addItem(ResUtils.getString(R.string.high_alarm_threshold))
                    .addItem(ResUtils.getString(R.string.medium_alarm_threshold))
                    .addItem(ResUtils.getString(R.string.low_alarm_threshold))
                    .setIsCenter(true)
                    .setOnSheetItemClickListener { dialog: BottomSheet, _: View?, threshold: Int, _: String? ->
                        dialog.dismiss()
                        when(threshold){
                            0 -> {
                                AlarmSettings.alarmThreshold = 90
                                ivSetThreshold.text = getString(R.string.set_alarm_threshold).format(90)
                            }
                            1 -> {
                                AlarmSettings.alarmThreshold = 80
                                ivSetThreshold.text = getString(R.string.set_alarm_threshold).format(80)
                            }
                            2 -> {
                                AlarmSettings.alarmThreshold = 70
                                ivSetThreshold.text = getString(R.string.set_alarm_threshold).format(70)
                            }
                        }
                    }
                    .build()
                    .show()
            }
            .addTo(glvAlarmSettings)

        val ivBoundEmail = glvAlarmSettings.createItemView(getString(R.string.bound_email).format(
            AlarmSettings.boundEmail ?: "未绑定邮箱")).apply{
                setItemEnabled(AlarmSettings.enableEmailAlarm)
        }
        val ivBoundPhoneNumber = glvAlarmSettings.createItemView(getString(R.string.bound_phone_number).format(
            AlarmSettings.boundPhoneNumber ?: "未绑定手机号")).apply {
                setItemEnabled(AlarmSettings.enableMessageAlarm)
        }
        XUIGroupListView.newSection(context)
            .setTitle(getString(R.string.alarm_method_settings))
            .setDescription(getString(R.string.alarm_method_settings_desc))
            /*
            * 弹窗告警设置
            * Default: false
            * */
            .addItemView(glvAlarmSettings.createItemView(getString(R.string.enable_pop_window_alarm)).apply {
                    accessoryType = XUICommonListItemView.ACCESSORY_TYPE_SWITCH
                    switch.isChecked = AlarmSettings.enablePopWindowAlarm
                    switch.setOnCheckedChangeListener { _, isChecked ->
                        if(isChecked) {
                            PopWindowAlarm.checkPermission(this@AlarmSettingsFragment) { result ->
                                if (result) {
                                    AlarmSettings.enablePopWindowAlarm = true
                                } else {
                                    ToastUtil.error(R.string.please_grant_pop_window_permission)
                                    switch.isChecked = false
                                }
                            }
                        }else{
                            AlarmSettings.enablePopWindowAlarm = false
                        }
                    }
            }){ }
            /*
            * 邮件告警设置
            * Default: false
            * */
            .addItemView(glvAlarmSettings.createItemView(getString(R.string.enable_email_alarm)).apply {
                accessoryType = XUICommonListItemView.ACCESSORY_TYPE_SWITCH
                switch.isChecked = AlarmSettings.enableEmailAlarm
                switch.setOnCheckedChangeListener { _, isChecked ->
                    AlarmSettings.enableEmailAlarm = isChecked
                    ivBoundEmail.setItemEnabled(isChecked)
                }
            }){ }
            .addItemView(ivBoundEmail){
                showEditBoundEmailDialog(context!!, ivBoundEmail)
            }
            /*
            * 短信告警设置
            * Default: false
            * */
            .addItemView(glvAlarmSettings.createItemView(getString(R.string.enable_message_alarm)).apply {
                accessoryType = XUICommonListItemView.ACCESSORY_TYPE_SWITCH
                switch.isChecked = AlarmSettings.enableMessageAlarm
                switch.setOnCheckedChangeListener { _, isChecked ->
                    AlarmSettings.enableMessageAlarm = isChecked
                    ivBoundPhoneNumber.setItemEnabled(isChecked)
                }
            }){ }
            .addItemView(ivBoundPhoneNumber){
                showEditBoundPhoneNumberDialog(context!!, ivBoundPhoneNumber)
            }
            .addTo(glvAlarmSettings)
    }

}