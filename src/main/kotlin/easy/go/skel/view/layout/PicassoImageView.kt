package easy.go.skel.view.layout

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import com.squareup.picasso.Transformation
import easy.go.skel.R
import easy.go.skel.view.extension.getRoundedBitmapDrawable

/**
 * Created by Dmitriy Khalturin <dmitry.halturin.86@gmail.com>
 * for easy_go_skel on 21.04.20 22:24.
 */
class PicassoImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

  private val imageView: ImageView
  private val loadingComponent: ProgressBar

  private val scaleType: Int

  private var isCircular: Boolean

  init {
    val a = context.obtainStyledAttributes(attrs, R.styleable.PicassoImageView, defStyleAttr, defStyleRes)

    imageView = ImageView(context, attrs).apply {
      layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)

      clipToOutline = true
    }

    loadingComponent = ProgressBar(context, attrs).apply {
      layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER)

      visibility = View.GONE
    }

    addView(imageView)
    addView(loadingComponent)

    scaleType = a.getInt(R.styleable.PicassoImageView_scaleType, -1)
    isCircular = a.getBoolean(R.styleable.PicassoImageView_circular, false)

    a.recycle()
  }

  // TODO: transformation don't work
  private val transformationToCircular = object : Transformation {
    override fun transform(source: Bitmap?): Bitmap? {
      val bitmapDrawable = source?.getRoundedBitmapDrawable(resources)

      return bitmapDrawable?.bitmap
    }

    override fun key(): String {
      return "circular())"
    }
  }

  private val picassoCallback = object : Callback {
    override fun onSuccess() {
      loadingComponent.visibility = View.GONE
    }

    override fun onError(exception: Exception?) {
      loadingComponent.visibility = View.GONE
    }
  }

  private fun RequestCreator.scaleTypeOf(scaleType: Int): RequestCreator {
    return when (scaleType) {
      0 -> centerCrop()
      1 -> centerInside()
      else -> this
    }
  }

  fun setImageUrl(url: String?) {
    Picasso.get().run {
      cancelRequest(imageView)

      loadingComponent.visibility = View.VISIBLE

      load(url)
        .apply { if (isCircular) transform(transformationToCircular) }
        .fit()
        .scaleTypeOf(scaleType)
        .into(imageView, picassoCallback)
    }
  }
}
