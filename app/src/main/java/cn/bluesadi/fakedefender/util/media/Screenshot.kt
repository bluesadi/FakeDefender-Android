package cn.bluesadi.fakedefender.util.media

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.util.DisplayMetrics
import android.view.WindowManager
import com.xuexiang.xui.XUI
import java.lang.Exception

/**
 * <pre>
 * author : 34r7hm4n
 * time   : 2021/9/23
 * desc   : 提供截屏操作的类
 * version: 1.0
</pre> *
 */
class Screenshot @SuppressLint("WrongConstant") constructor(
    private val resultData: Intent
) {
    var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private val imageReader: ImageReader
    private val screenWidth: Int
    private val screenHeight: Int
    private val screenDensity: Int

    fun destroy() {
        virtualDisplay!!.release()
        imageReader.close()
        mediaProjection!!.stop()
    }

    private fun setUpMediaProjection() {
        val mediaProjectionManager =
            XUI.getContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        mediaProjection = mediaProjectionManager.getMediaProjection(Activity.RESULT_OK, resultData)
    }

    private fun startVirtual() {
        if (mediaProjection == null) {
            setUpMediaProjection()
        }
        virtualDisplay()
    }

    private fun virtualDisplay() {
        virtualDisplay = mediaProjection!!.createVirtualDisplay(
            "screen-mirror",
            screenWidth,
            screenHeight,
            screenDensity,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader.surface,
            null,
            null
        )
    }

    fun startScreenShot() {
        startVirtual()
    }

    /**
     * 截取一帧屏幕
     *
     * @return 截取导的 Bitmap
     */
    fun capture(): Bitmap? {
        try {
            imageReader.acquireLatestImage().use { image ->
                if (image != null) {
                    val width = image.width
                    val height = image.height
                    val planes = image.planes
                    val buffer = planes[0].buffer
                    val pixelStride = planes[0].pixelStride
                    val rowStride = planes[0].rowStride
                    val rowPadding = rowStride - pixelStride * width
                    var bitmap = Bitmap.createBitmap(
                        width + rowPadding / pixelStride,
                        height,
                        Bitmap.Config.ARGB_8888
                    )
                    bitmap.copyPixelsFromBuffer(buffer)
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height)
                    image.close()
                    return bitmap
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    init {
        val metrics = DisplayMetrics()
        val windowManager = XUI.getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(metrics)
        screenDensity = metrics.densityDpi
        screenWidth = metrics.widthPixels
        screenHeight = metrics.heightPixels
        imageReader = ImageReader.newInstance(screenWidth, screenHeight, PixelFormat.RGBA_8888, 10)
    }
}
