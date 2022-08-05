package cn.bluesadi.fakedefender.util

import android.graphics.Color
import com.xuexiang.xui.widget.grouplist.XUICommonListItemView

/**
 *
 * @author 34r7hm4n
 * @since 2022/8/3 18:22
 */
fun XUICommonListItemView.setItemEnabled(enabled: Boolean){
    isEnabled = enabled
    textView.setTextColor(if(enabled) Color.BLACK else Color.GRAY)
}