package cn.bluesadi.fakedefender.util

import android.content.ContentValues
import android.graphics.*
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.xuexiang.xui.XUI
import org.json.JSONObject
import android.graphics.Bitmap
import android.util.Base64
import java.io.*


/**
 *
 * @author 34r7hm4n
 * @since 2022/3/21 8:28
 */
object ImageUtil {

    fun saveImage(bitmap: Bitmap) {
        val context = XUI.getContext()
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            val values = contentValues()
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/")
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            // RELATIVE_PATH and IS_PENDING are introduced in API 29.

            val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
            }
        } else {
            val directory = Environment.getExternalStorageDirectory()
            // getExternalStorageDirectory is deprecated in API 29

            if (!directory.exists()) {
                directory.mkdirs()
            }
            val fileName = System.currentTimeMillis().toString() + ".png"
            val file = File(directory, fileName)
            saveImageToStream(bitmap, FileOutputStream(file))
            val values = contentValues()
            values.put(MediaStore.Images.Media.DATA, file.absolutePath)
            // .DATA is deprecated in API 29
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        }
    }

    private fun contentValues() : ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return values
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun drawRect(bitmap: Bitmap, rect: Rect, score: Int){
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE    //不填充
        paint.strokeWidth = 10.0f  //线的宽度
        rect.apply {
            canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
        }
    }

    fun convertToBase64(bitmap: Bitmap) : String {
        ByteArrayOutputStream().use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            return Base64.encodeToString(out.toByteArray(), Base64.DEFAULT)
        }
    }

}