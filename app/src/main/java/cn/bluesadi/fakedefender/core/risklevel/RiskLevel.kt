package cn.bluesadi.fakedefender.core.risklevel

import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.data.AlarmSettings
import cn.bluesadi.fakedefender.data.GeneralSettings
import com.xuexiang.xui.utils.ResUtils
import kotlin.math.min

/**
 *
 * @author 34r7hm4n
 * @since 2022/4/24 20:50
 */
enum class RiskLevel(val code: Int, val display: String, val color: Int) {

    NO_RISK(0, ResUtils.getString(R.string.no_risk), ResUtils.getColor(R.color.no_risk)),
    LOW_RISK(1,  ResUtils.getString(R.string.low_risk), ResUtils.getColor(R.color.low_risk)),
    MEDIUM_RISK(2, ResUtils.getString(R.string.medium_risk), ResUtils.getColor(R.color.medium_risk)),
    HIGH_RISK(3, ResUtils.getString(R.string.high_risk), ResUtils.getColor(R.color.high_risk)),
    EXTREME_HIGH_RISK(4,  ResUtils.getString(R.string.extreme_high_risk), ResUtils.getColor(R.color.high_risk));

//    val SOFTWARE_CATEGORY = arrayOf(
//        "视频", "阅读", "社交", "理财", "购物", "工具",
//        "摄影", "音乐", "生活", "教育", "旅游出行", "系统", "健康", "办公", "特色分类"
//    )
//    val GAME_CATEGORY =
//        arrayOf("休闲益智", "网络游戏", "棋牌中心", "电子竞技", "动作冒险", "经营策略", "角色扮演", "体育竞速", "军事战争", "特色分类")

    val detectionInterval: Long
        get() {
            return when(GeneralSettings.detectionFrequency){
                /* 高频率 */
                0 -> {
                    when(this){
                        NO_RISK -> 5000
                        LOW_RISK -> 3000
                        MEDIUM_RISK -> 1200
                        HIGH_RISK -> 1000
                        EXTREME_HIGH_RISK -> 500
                    }
                }
                /* 中频率 */
                1 -> {
                    when(this){
                        NO_RISK -> 8000
                        LOW_RISK -> 5000
                        MEDIUM_RISK -> 2000
                        HIGH_RISK -> 1200
                        EXTREME_HIGH_RISK -> 1000
                    }
                }
                /* 低频率 */
                else -> {
                    when(this){
                        NO_RISK -> 10000
                        LOW_RISK -> 8000
                        MEDIUM_RISK -> 5000
                        HIGH_RISK -> 2000
                        EXTREME_HIGH_RISK -> 1000
                    }
                }
            }
        }

    fun next() : RiskLevel{
        return getFromCode(min(code + 1, 4))
    }

    companion object{

        private val HIGH_RISK_CATEGORIES = arrayOf("视频", "办公", "社交")

        private val MEDIUM_RISK_CATEGORIES = arrayOf("理财", "生活", "购物")

        private val LOW_RISK_CATEGORIES = arrayOf("旅行出行", "摄影", "阅读")

        fun getFromCode(code: Int) : RiskLevel{
            values().forEach {
                if(it.code == code){
                    return it
                }
            }
            return NO_RISK
        }

        fun getFromCategory(category: String) : RiskLevel{
            if(category.isEmpty()) return NO_RISK
            return when(category){
                in HIGH_RISK_CATEGORIES -> HIGH_RISK
                in MEDIUM_RISK_CATEGORIES -> MEDIUM_RISK
                in LOW_RISK_CATEGORIES -> LOW_RISK
                else -> NO_RISK
            }
        }
    }

}