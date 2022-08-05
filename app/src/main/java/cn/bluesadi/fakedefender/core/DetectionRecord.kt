package cn.bluesadi.fakedefender.core

import android.graphics.*
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.core.face.FilteredReason
import cn.bluesadi.fakedefender.data.GeneralSettings
import com.xuexiang.xui.utils.ResUtils
import java.lang.Integer.max

/**
 *
 * @author 34r7hm4n
 * @since 2022/3/21 15:56
 */
class DetectionRecord(
    val timestamp: Long,
    val facesScores: List<FaceScore>,
    bitmap: Bitmap
) {

    data class FaceScore(
        val box: Rect,
        val score: Int
    )

    val riskScore: Int = max(if(facesScores.isEmpty()) 0 else facesScores.maxOf { it.score }, 0)

    val riskColor: Int = getRiskColor(riskScore)

    private fun getRiskColor(riskScore: Int): Int{
        return ResUtils.getColor(when{
            riskScore < 0 -> R.color.no_risk    // filtered
            riskScore >= GeneralSettings.highScoreThreshold -> R.color.high_risk
            //riskScore >= GeneralSettings.mediumScoreThreshold -> R.color.medium_risk
            else -> R.color.low_risk
        })
    }

    val markedImage: Bitmap by lazy {
        facesScores.forEach { face ->
            drawRect(bitmap, face.box, face.score, getRiskColor(face.score))
        }
        bitmap
    }

    private fun drawRect(bitmap: Bitmap, rect: Rect, score: Int, riskColor: Int){
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            color = riskColor
            style = Paint.Style.STROKE
            strokeWidth = 10.0f
        }
        val textPaint = Paint().apply {
            color = riskColor
            textSize = (rect.bottom - rect.top) / 4f
        }
        rect.apply {
            canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
            canvas.drawText(
                when(score){
                    FilteredReason.BLUR.reasonCode -> FilteredReason.BLUR.reasonDesc
                    FilteredReason.ROTATION.reasonCode -> FilteredReason.ROTATION.reasonDesc
                    FilteredReason.SMALL.reasonCode -> FilteredReason.SMALL.reasonDesc
                    else -> score.toString() },
                left.toFloat() + (right - left) / 8f,
                top.toFloat() + (bottom - top) / 4f,
                textPaint
            )
        }
    }

}