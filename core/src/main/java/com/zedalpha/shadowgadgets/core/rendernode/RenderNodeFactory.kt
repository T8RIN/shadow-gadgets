package com.zedalpha.shadowgadgets.core.rendernode

import android.graphics.Matrix
import android.graphics.Outline
import android.os.Build

object RenderNodeFactory {

    val isOpenForBusiness = Build.VERSION.SDK_INT >= 29 ||
            (Build.VERSION.SDK_INT != 28 && testRenderNode())

    fun newInstance(name: String? = null): RenderNodeWrapper {
        if (!isOpenForBusiness) throw IllegalStateException("Unavailable")
        return innerNewInstance(name)
    }

    private fun innerNewInstance(name: String?): RenderNodeWrapper =
        when (Build.VERSION.SDK_INT) {
            21, 22 -> RenderNodeApi21(name)
            in 23..27 -> RenderNodeApi23(name)
            28 -> throw IllegalStateException("That's unpossible!")
            else -> RenderNodeApi29(name)
        }

    private fun testRenderNode() = try {
        with(innerNewInstance("TestInstance")) {
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