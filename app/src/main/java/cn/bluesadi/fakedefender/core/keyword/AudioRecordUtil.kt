package cn.bluesadi.fakedefender.core.keyword

/**
 *
 * @author 34r7hm4n
 * @since 2022/5/7 19:45
 */
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import androidx.core.app.ActivityCompat
import cn.bluesadi.fakedefender.util.d
import com.xuexiang.xui.XUI


@SuppressLint("MissingPermission")
class AudioRecordUtil {
    private var audioRecord: AudioRecord
    private val bufferSize: Int = AudioRecord.getMinBufferSize(
        SAMPLE_RATE,
        AudioFormat.CHANNEL_IN_DEFAULT,
        AudioFormat.ENCODING_PCM_16BIT
    )
    private var readSize = 0
    private var isStart = false
    private lateinit var onRecordListener: OnRecordListener

    companion object{
        private const val SAMPLE_RATE = 16000
    }

    fun setOnRecordListener(onRecordListener: OnRecordListener) {
        this.onRecordListener = onRecordListener
    }

    interface OnRecordListener {
        fun readByte(data: ByteArray, size: Int)
    }

    /**
     * 开始
     */
    fun startRecord() {
        Thread {
            isStart = true
            audioRecord.startRecording()
            val audioData = ByteArray(bufferSize)
            while (isStart) {
                readSize = audioRecord.read(audioData, 0, bufferSize)
                onRecordListener.readByte(audioData, readSize)
            }
            //释放
            audioRecord.stop()
            audioRecord.release()
        }.start()
    }

    /**
     * 停止
     */
    fun stopRecord() {
        isStart = false
    }


    init {
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )
    }
}