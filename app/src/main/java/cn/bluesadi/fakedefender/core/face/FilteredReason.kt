package cn.bluesadi.fakedefender.core.face

/**
 *
 * @author 34r7hm4n
 * @since 2022/8/3 20:05
 */
enum class FilteredReason(val reasonCode: Int, val reasonDesc: String) {

    BLUR(-100, "模糊"),
    SMALL(-200, "过小"),
    ROTATION(-300, "侧脸")

}