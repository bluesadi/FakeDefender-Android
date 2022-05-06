package cn.bluesadi.fakedefender.util

import android.graphics.Bitmap
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.util.concurrent.Executors

/**
 *
 * @author 34r7hm4n
 * @since 2022/5/6 13:22
 */
object FaceDetector {

    private val detector = FaceDetection.getClient(
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setExecutor(Executors.newFixedThreadPool(20))
            .build()
    )

    fun detectFaces(bitmap: Bitmap, listener: OnSuccessListener<MutableList<Face>>) {
        val image = InputImage.fromBitmap(bitmap, 0)
        detector.process(image).addOnSuccessListener(listener)
    }

}