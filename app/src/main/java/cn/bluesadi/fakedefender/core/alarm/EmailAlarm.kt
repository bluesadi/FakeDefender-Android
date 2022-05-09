package cn.bluesadi.fakedefender.core.alarm

import cn.bluesadi.fakedefender.adapter.DetectionRecordListAdapter
import cn.bluesadi.fakedefender.data.AlarmSettings
import cn.bluesadi.fakedefender.data.Config
import cn.bluesadi.fakedefender.network.NetworkServices

/**
 *
 * @author 34r7hm4n
 * @since 2022/5/9 10:32
 */
object EmailAlarm {

    fun alarm(){
        AlarmSettings.boundEmail?.let { email ->
            Config.phoneNumber?.let { phone ->
                NetworkServices.emailAlarm(phone, email, DetectionRecordListAdapter.INSTANCE.data[0].markedImage)
            }
        }
    }

}