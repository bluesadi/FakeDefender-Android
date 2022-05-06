package cn.bluesadi.fakedefender.util

import android.util.Log
import cn.bluesadi.fakedefender.FakeDefenderApp

/**
 *
 * @author 34r7hm4n
 * @since 2022/5/5 16:07
 */

fun d(msg: String){
    if(FakeDefenderApp.DEBUG)
        Log.d("DEBUG-FAKE-DEFENDER", msg)
}