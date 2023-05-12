package com.zedalpha.shadowgadgets.core.rendernode

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Outline
import android.os.Build
import com.zedalpha.shadowgadgets.core.Shadow


interface RenderNodeWrapper : Shadow {
    fun setClipToBounds(clipToBounds: Boolean): Boolean
    fun setProjectBackwards(shouldProject: Boolean): Boolean
    fun setProjectionReceiver(shouldReceive: Boolean): Boolean
    fun setPosition(left: Int, top: Int, right: Int, bottom: Int): Boolean
    fun beginRecording(width: Int, height: Int): Canvas
    fun endRecording(canvas: Canvas)
    fun hasDisplayList(): Boolean
}

object RenderNodeFactory {

    val isOpenForBusiness = Build.VERSION.SDK_INT >= 29 ||
            (Build.VERSION.SDK_INT != 28 && testRenderNode())

    fun newInstance(): RenderNodeWrapper {
        if (!isOpenForBusiness) throw IllegalStateException("Unavailable")
        return innerNewInstance()
    }

    private fun innerNewInstance() = when (Build.VERSION.SDK_INT) {
        21, 22 -> RenderNodeApi21()
        in 23..27 -> RenderNodeApi23()
        28 -> throw IllegalStateException("That's unpossible!")
        else -> RenderNodeApi29()
    }

    private fun testRenderNode() = try {
        with(innerNewInstance()) {
            alpha = alpha
            cameraDistance = cameraDistance
            elevation = elevation
            pivotX = pivotX
            pivotY = pivotY
            rotationX = rotationX
            rotationY = rotationY
            rotationZ = rotationZ
            scaleX = scaleX
            scaleY = scaleY
            translationX = translationX
            translationY = translationY
            translationZ = translationZ
            ambientColor = ambientColor
            spotColor = spotColor
            setOutline(Outline())
            hasIdentityMatrix()
            getMatrix(Matrix())
            setClipToBounds(false)
            setProjectBackwards(true)
            setProjectionReceiver(true)
            endRecording(beginRecording(0, 0))
            hasDisplayList()
        }
        true
    } catch (e: Throwable) {
        false
    }
}

internal const val RenderNodeName = "ShadowGadgets"