package cn.bluesadi.fakedefender.face

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import androidx.work.*
import cn.bluesadi.fakedefender.fragment.MonitorMainFragment
import cn.bluesadi.fakedefender.network.NetworkServices
import com.xuexiang.xui.XUI
import com.xuexiang.xutil.display.ScreenUtils
import kotlin.math.min

/**
 *
 * @author 34r7hm4n
 * @since 2022/4/11 9:24
 */
class MonitorWorker(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    companion object {

        const val UNIQUE_WORK_NAME = "MONITOR"

        const val DETECTION_INTERVAL = 500L

        fun buildWorkRequest() : OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<MonitorWorker>().build()
        }

    }

    private val handler = Handler(Looper.getMainLooper())

//    fun screenshot() : Bitmap{
//    }

    override fun doWork(): Result {
        while (true){
            if(isStopped) break
            handler.post {
//                NetworkServices.predict(screenshot()){
//                    DetectionResultHandler.handle(it)
//                }
                MonitorMainFragment.instance?.get()?.updateScore(MonitorManager.riskScore)
            }
            Thread.sleep(DETECTION_INTERVAL)
        }
        return Result.success()
    }

}