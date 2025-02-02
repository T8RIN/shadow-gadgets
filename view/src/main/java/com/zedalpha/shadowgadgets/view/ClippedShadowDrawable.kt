package com.zedalpha.shadowgadgets.view

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Matrix
import android.graphics.Outline
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import com.zedalpha.shadowgadgets.core.ClippedShadow
import com.zedalpha.shadowgadgets.core.DefaultShadowColorInt
import com.zedalpha.shadowgadgets.core.ShadowColorFilter
import kotlin.math.roundToInt
import com.zedalpha.shadowgadgets.core.PathProvider as CorePathProvider

open class ClippedShadowDrawable private constructor(
    private val clippedShadow: ClippedShadow
) : Drawable() {

    fun interface PathProvider {
        fun getPath(path: Path)
    }

    companion object {
        internal fun PathProvider.toCoreProvider() =
            CorePathProvider { getPath(it) }
    }

    constructor(ownerView: View, provider: PathProvider? = null) :
            this(ClippedShadow(ownerView, provider?.toCoreProvider()))

    @RequiresApi(29)
    constructor(provider: PathProvider? = null) :
            this(ClippedShadow(provider?.toCoreProvider()))

    private var colorFilter: ShadowColorFilter? = null

    @get:ColorInt
    @setparam:ColorInt
    var color: Int = DefaultShadowColorInt
        set(value) {
            if (field == value) return
            field = value
            if (value != DefaultShadowColorInt) {
                val filter =
                    colorFilter ?: ShadowColorFilter().also { colorFilter = it }
                filter.color = value
            } else {
                colorFilter = null
            }
        }

    override fun setAlpha(alpha: Int) {
        clippedShadow.alpha = alpha / 255F
    }

    override fun getAlpha(): Int {
        return (255 * clippedShadow.alpha).roundToInt()
    }

    var cameraDistance: Float
        get() = clippedShadow.cameraDistance
        set(value) {
            clippedShadow.cameraDistance = value
        }

    var elevation: Float
        get() = clippedShadow.elevation
        set(value) {
            clippedShadow.elevation = value
        }

    var pivotX: Float
        get() = clippedShadow.pivotX
        set(value) {
            clippedShadow.pivotX = value
        }

    var pivotY: Float
        get() = clippedShadow.pivotY
        set(value) {
            clippedShadow.pivotY = value
        }

    var rotationX: Float
        get() = clippedShadow.rotationX
        set(value) {
            clippedShadow.rotationX = value
        }

    var rotationY: Float
        get() = clippedShadow.rotationY
        set(value) {
            clippedShadow.rotationY = value
        }

    var rotationZ: Float
        get() = clippedShadow.rotationZ
        set(value) {
            clippedShadow.rotationZ = value
        }

    var scaleX: Float
        get() = clippedShadow.scaleX
        set(value) {
            clippedShadow.scaleX = value
        }

    var scaleY: Float
        get() = clippedShadow.scaleY
        set(value) {
            clippedShadow.scaleY = value
        }

    var translationX: Float
        get() = clippedShadow.translationX
        set(value) {
            clippedShadow.translationX = value
        }

    var translationY: Float
        get() = clippedShadow.translationY
        set(value) {
            clippedShadow.translationY = value
        }

    var translationZ: Float
        get() = clippedShadow.translationZ
        set(value) {
            clippedShadow.translationZ = value
        }

    @get:ColorInt
    @setparam:ColorInt
    var ambientColor: Int
        get() = clippedShadow.ambientColor
        set(value) {
            clippedShadow.ambientColor = value
        }

    @get:ColorInt
    @setparam:ColorInt
    var spotColor: Int
        get() = clippedShadow.spotColor
        set(value) {
            clippedShadow.spotColor = value
        }

    fun hasIdentityMatrix(): Boolean =
        clippedShadow.hasIdentityMatrix()

    fun getMatrix(outMatrix: Matrix) {
        clippedShadow.getMatrix(outMatrix)
    }

    fun setOutline(outline: Outline?) {
        clippedShadow.setOutline(outline)
    }

    override fun draw(canvas: Canvas) {
        when (val filter = colorFilter) {
            null -> clippedShadow.draw(canvas)
            else -> filter.draw(canvas, clippedShadow)
        }
    }

    fun dispose() {
        clippedShadow.dispose()
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {}
}