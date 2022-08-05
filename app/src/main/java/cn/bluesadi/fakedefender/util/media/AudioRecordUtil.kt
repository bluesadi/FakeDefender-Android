package cn.bluesadi.fakedefender.util.media

/**
 *
 * @author 34r7hm4n
 * @since 2022/5/7 19:45
 */
import android.annotation.SuppressLint
import android.media.*
import android.os.Build
import android.os.Process
import androidx.annotation.RequiresApi
import cn.bluesadi.fakedefender.core.MonitorManager
import cn.bluesadi.fakedefender.util.d

@RequiresApi(Build.VERSION_CODES.Q)
class AudioRecordUtil {

    private val bufferSize: Int = AudioRecord.getMinBufferSize(
        SAMPLE_RATE,
        AudioFormat.CHANNEL_CONFIGURATION_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )
    private var readSize = 0
    private var isRecording = false
    private lateinit var onRecordListener: OnRecordListener

    private var audioRecord: AudioRecord? = null
    private var thread: Thread? = null

    companion object{
        private const val SAMPLE_RATE = 16000
    }

    fun setOnRecordListener(onRecordListener: OnRecordListener) {
        this.onRecordListener = onRecordListener
    }

    interface OnRecordListener {
        fun readByte(data: ByteArray, size: Int)
    }

    @SuppressLint("MissingPermission")  // has requested permission when starting monitoring
    fun buildRecord() : AudioRecord{
        val builder = AudioRecord.Builder()
            .setAudioFormat(AudioFormat.Builder()
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .setSampleRate(SAMPLE_RATE)
                .build())
            .setBufferSizeInBytes(bufferSize)
        MonitorManager.screenshot!!.mediaProjection!!.let {
            builder.setAudioPlaybackCaptureConfig(
                AudioPlaybackCaptureConfiguration.Builder(it)
                    .addMatchingUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
        return builder.build()
    }

    fun initRecord() {
        audioRecord = buildRecord().apply {
            startRecording()
        }
    }

    /**
     * 开始
     */
    fun startRecord() {
        thread = Thread {
            Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO)
            isRecording = true
            audioRecord?.apply {
                val audioData = ByteArray(bufferSize)
                while (isRecording && recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                    readSize = read(audioData, 0, bufferSize)
                    if (readSize > 0) {
                        onRecordListener.readByte(audioData, readSize)
                    }
                    Thread.sleep(40)
                }
            }
        }
        thread!!.start()
    }

    /**
     * 停止
     */
    fun stopRecord() {
        isRecording = false
        audioRecord?.apply {
            if (recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                val tmp = ByteArray(bufferSize)
                while(read(tmp, 0, bufferSize) > 0)
                stop()
                d("audioRecord.stop()")
            }
            if (state == AudioRecord.STATE_INITIALIZED) {
                release()
                d("audioRecord.release()")
            }
            println("DEBUG-1: " + recordingState)
            println("DEBUG-2" + state)
        }
        audioRecord = null
        thread = null
    }
}