package cn.bluesadi.fakedefender.face

import android.graphics.*
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.util.ImageUtil
import cn.bluesadi.fakedefender.data.Settings
import com.xuexiang.xui.utils.ResUtils

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

    val riskScore: Int = if(facesScores.isEmpty()) 0 else facesScores.maxOf { it.score }

    val riskColor: Int = ResUtils.getColor(when{
        riskScore >= Settings.highScoreThreshold -> R.color.high_risk
        riskScore >= Settings.mediumScoreThreshold -> R.color.medium_risk
        else -> R.color.low_risk
    })

    val markedImage: Bitmap by lazy {
        facesScores.forEach { face ->
            drawRect(bitmap, face.box, face.score)
        }
        bitmap
    }

    private fun drawRect(bitmap: Bitmap, rect: Rect, score: Int){
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            color = riskColor
            style = Paint.Style.STROKE
            strokeWidth = 10.0f
        }
        val textPaint = Paint().apply {
            color = riskColor
            textSize = bitmap.height / 8f
        }
        rect.apply {
            canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
            canvas.drawText(
                score.toString(),
                left.toFloat() + bitmap.width / 50f,
                top.toFloat() + bitmap.height / 8f,
                textPaint
            )
        }
    }

}