package cn.bluesadi.fakedefender.data

import com.xuexiang.xutil.data.SPUtils

/**
 *
 * @author 34r7hm4n
 * @since 2022/4/15 20:22
 */
object Config {

    private const val FIRST_RUNNING_TIME_KEY = "first_running_time"
    private const val LAST_RUNNING_TIME_KEY = "last_running_time"
    private const val LAST_PHONE_NUMBER = "last_phone_number"
    private const val COOKIE_KEY = "cookie"

    val firstRunningTime
        get() = run {
            if (!config.contains(FIRST_RUNNING_TIME_KEY)) {
                config.edit().apply {
                    putLong(FIRST_RUNNING_TIME_KEY, System.currentTimeMillis())
                }.apply()
            }
            config.getLong(FIRST_RUNNING_TIME_KEY, 0)
        }

    var lastRunningTime
        get() = config.getLong(LAST_RUNNING_TIME_KEY, 0)
        set(value) = config.edit().apply {
            putLong(LAST_RUNNING_TIME_KEY, value)
        }.apply()

    var phoneNumber
        get() = config.getString(LAST_PHONE_NUMBER, "")
        set(value) = config.edit().apply {
            putString(LAST_PHONE_NUMBER, value)
        }.apply()

    var cookie
        get() = config.getString(COOKIE_KEY, null)
        set(value) = config.edit().apply {
            putString(COOKIE_KEY, value)
        }.apply()

}