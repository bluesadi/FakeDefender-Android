package cn.bluesadi.fakedefender.util

import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @author 34r7hm4n
 * @since 2022/4/16 15:18
 */
object TimeUtil {

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)

    fun getDaysFrom(startTime: Long) : Long{
        return (System.currentTimeMillis() - startTime) / 86400000
    }

    fun formatTime(timestamp: Long) : String{
        return formatter.format(timestamp)
    }

}