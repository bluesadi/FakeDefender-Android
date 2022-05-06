package cn.bluesadi.fakedefender.fragment

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.base.BaseHomeFragment
import cn.bluesadi.fakedefender.databinding.FragmentUploadBinding
import cn.bluesadi.fakedefender.core.DetectionRecord
import cn.bluesadi.fakedefender.network.NetworkServices
import cn.bluesadi.fakedefender.util.pictureselector.GlideEngine
import cn.bluesadi.fakedefender.util.ImageUtil
import cn.bluesadi.fakedefender.util.ToastUtil
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.xuexiang.xpage.annotation.Page
import com.xuexiang.xui.widget.button.roundbutton.RoundButton
import com.luck.picture.lib.entity.LocalMedia

import com.luck.picture.lib.interfaces.OnResultCallbackListener

import com.luck.picture.lib.config.SelectMimeType

import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectModeConfig
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog


@Page(name = "上传检测")
class UploadFragment : BaseHomeFragment<FragmentUploadBinding>() {

    companion object {
        fun showDetectionResultDialog(context: Context, record: DetectionRecord){
            val dialog: MaterialDialog = MaterialDialog.Builder(context)
                .customView(R.layout.dialog_show_detection_result, true)
                .title("检测结果")
                .build()
            val ivDetectionResult = dialog.findViewById<ImageView>(R.id.iv_detection_result)
            val btnSaveImage = dialog.findViewById<Button>(R.id.btn_save_image)
            ivDetectionResult.setImageBitmap(record.markedImage)
            ivDetectionResult.setOnClickListener { dialog.dismiss() }
            btnSaveImage.setOnClickListener {
                Thread {
                    ImageUtil.saveImage(record.markedImage)
                }.start()
                ToastUtil.info(R.string.save_image_success)
            }
            dialog.show()
        }
    }

    private lateinit var btnUploadImage: RoundButton
    private lateinit var ivDetectionResult: ImageView

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup
    ): FragmentUploadBinding {
        return FragmentUploadBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        binding!!.let {
            btnUploadImage = it.btnUploadImage
            ivDetectionResult = it.ivDetectionResult
        }
    }

    override fun initListeners() {
        btnUploadImage.setOnClickListener {
            PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.createGlideEngine())
                .setSelectionMode(SelectModeConfig.SINGLE)
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: ArrayList<LocalMedia>) {
                        if(result.size > 0){
                            val uri = Uri.parse(result[0].availablePath)
                            val bitmap = BitmapFactory.decodeStream(context!!.contentResolver.openInputStream(uri))
                                .copy(Bitmap.Config.ARGB_8888, true)
                            val dialog = MaterialDialog.Builder(context!!)
                                .cancelable(false)
                                .iconRes(R.drawable.ic_tip)
                                .limitIconToDefaultSize()
                                .title(R.string.tip)
                                .content(R.string.wait_for_check_result)
                                .progress(true, 0)
                                .progressIndeterminateStyle(false)
                                .show()
                            NetworkServices.predict(bitmap) {
                                showDetectionResultDialog(context!!, it)
                                dialog.dismiss()
                            }
                        }
                    }
                    override fun onCancel() {}
                })
        }
    }


}