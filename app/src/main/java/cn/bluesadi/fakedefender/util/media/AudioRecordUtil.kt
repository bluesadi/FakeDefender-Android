package cn.bluesadi.fakedefender.util.media

/**
 *
 * @author 34r7hm4n
 * @since 2022/5/7 19:45
 */
import android.annotation.SuppressLint
import android.media.*
import android.os.Build
import androidx.annotation.RequiresApi
import cn.bluesadi.fakedefender.core.MonitorManager
import cn.bluesadi.fakedefender.util.d


@RequiresApi(Build.VERSION_CODES.Q)
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
        val builder = AudioRecord.Builder()
            .setAudioFormat(AudioFormat.Builder()
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .setSampleRate(SAMPLE_RATE)
                .build())
            .setBufferSizeInBytes(bufferSize)
        MonitorManager.screenshot?.mediaProjection?.let {
            d("哈哈哈: $bufferSize")
            builder.setAudioPlaybackCaptureConfig(
                AudioPlaybackCaptureConfiguration.Builder(it)
                .addMatchingUsage(AudioAttributes.USAGE_MEDIA)
                .build()
            )
        } ?: d("无语")
        audioRecord = builder.build()
    }
}