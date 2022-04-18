package cn.bluesadi.fakedefender.util.pictureselector

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import cn.bluesadi.fakedefender.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.interfaces.OnCallbackListener
import com.luck.picture.lib.utils.ActivityCompatHelper


/**
 * @author：luck
 * @date：2019-11-13 17:02
 * @describe：Glide加载引擎
 */
class GlideEngine private constructor() : ImageEngine {
    /**
     * 加载图片
     *
     * @param context   上下文
     * @param url       资源url
     * @param imageView 图片承载控件
     */
    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return
        }
        Glide.with(context)
            .load(url)
            .into(imageView)
    }

    /**
     * 加载指定url并返回bitmap
     *
     * @param context   上下文
     * @param url       资源url
     * @param maxWidth  资源最大加载尺寸
     * @param maxHeight 资源最大加载尺寸
     * @param call      回调接口
     */
    override fun loadImageBitmap(
        context: Context,
        url: String,
        maxWidth: Int,
        maxHeight: Int,
        call: OnCallbackListener<Bitmap>
    ) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return
        }
        Glide.with(context)
            .asBitmap()
            .override(maxWidth, maxHeight)
            .load(url)
            .into(object : CustomTarget<Bitmap?>() {
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    call.onCall(null)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    call.onCall(resource)
                }
            })
    }

    /**
     * 加载相册目录封面
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    override fun loadAlbumCover(context: Context, url: String, imageView: ImageView) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return
        }
        Glide.with(context)
            .asBitmap()
            .load(url)
            .override(180, 180)
            .sizeMultiplier(0.5f)
            .transform(CenterCrop(), RoundedCorners(8))
            .placeholder(R.drawable.ps_image_placeholder)
            .into(imageView)
    }

    /**
     * 加载图片列表图片
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return
        }
        Glide.with(context)
            .load(url)
            .override(200, 200)
            .centerCrop()
            .placeholder(R.drawable.ps_image_placeholder)
            .into(imageView)
    }

    override fun pauseRequests(context: Context) {
        Glide.with(context).pauseRequests()
    }

    override fun resumeRequests(context: Context) {
        Glide.with(context).resumeRequests()
    }

    companion object {
        private var instance: GlideEngine? = null
        fun createGlideEngine(): GlideEngine? {
            if (null == instance) {
                synchronized(GlideEngine::class.java) {
                    if (null == instance) {
                        instance = GlideEngine()
                    }
                }
            }
            return instance
        }
    }
}