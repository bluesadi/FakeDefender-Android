package cn.bluesadi.fakedefender.fragment

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.drawToBitmap
import cn.bluesadi.fakedefender.R
import cn.bluesadi.fakedefender.adapter.DetectionRecordListAdapter
import cn.bluesadi.fakedefender.base.BaseHomeFragment
import cn.bluesadi.fakedefender.databinding.FragmentUploadBinding
import cn.bluesadi.fakedefender.face.DetectionRecord
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
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import java.time.LocalDateTime


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
//        ivDetectionResult.setOnLongClickListener {
//            BottomSheet.BottomListSheetBuilder(activity)
//                //.setTitle(R.string.please_select)
//                .addItem(getString(R.string.save_image))
//                .setIsCenter(true)
//                .setOnSheetItemClickListener { dialog, _, position, _ ->
//                    dialog.dismiss()
//                    when(position){
//                        0 -> {
//                            Thread {
//                                ImageUtil.saveImage(it.drawToBitmap(Bitmap.Config.ARGB_8888))
//                            }.start()
//                            ToastUtil.info(R.string.save_image_success)
//                        }
//                    }
//                }
//                .build()
//                .show()
//            true
//        }
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
                            val image = InputImage.fromBitmap(bitmap, 0)
                            val detector = FaceDetection.getClient(
                                FaceDetectorOptions.Builder()
                                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                                    .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                                    .build()
                            )
                            val dialog = MaterialDialog.Builder(context!!)
                                .cancelable(false)
                                .iconRes(R.drawable.ic_tip)
                                .limitIconToDefaultSize()
                                .title(R.string.tip)
                                .content(R.string.wait_for_check_result)
                                .progress(true, 0)
                                .progressIndeterminateStyle(false)
                                .show()
                            detector.process(image).addOnSuccessListener { faces ->
                                if(faces.isNotEmpty()){
                                    NetworkServices.predict(bitmap){
//                                        ivDetectionResult.visibility = View.VISIBLE
//                                        ivDetectionResult.setImageBitmap(it.markedImage)
                                        showDetectionResultDialog(context!!, it)
                                    }
                                }
//                                faces.forEach { face ->
//                                    ImageUtil.drawRect(bitmap, face.boundingBox)
//                                }
//                                ivDetectionResult.visibility = View.VISIBLE
//                                ivDetectionResult.setImageBitmap(bitmap)
//                                // TODO
//                                DetectionRecordListAdapter.INSTANCE.addRecord(
//                                    DetectionRecord(System.currentTimeMillis(), mutableListOf<DetectionRecord.FaceScore>().apply {
//                                        faces.forEach {
//                                            add(DetectionRecord.FaceScore(it.boundingBox, 100))
//                                        }
//                                    }, bitmap)
//                                , true)
                                dialog.dismiss()
                            }.addOnFailureListener {
                                ToastUtil.error(R.string.face_detection_failure)
                                dialog.dismiss()
                            }
                        }
                    }
                    override fun onCancel() {}
                })
        }
    }


}