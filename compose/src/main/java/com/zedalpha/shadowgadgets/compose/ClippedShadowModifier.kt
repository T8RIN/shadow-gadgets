package com.zedalpha.shadowgadgets.compose

import android.os.Build
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp

fun Modifier.clippedShadow(
    elevation: Dp,
    shape: Shape = RectangleShape,
    ambientColor: Color = DefaultShadowColor,
    spotColor: Color = DefaultShadowColor,
    compatColor: Color = DefaultShadowColor,
    forceCompatColor: Boolean = false
) = if (elevation.value <= 0F) this else composed(
    factory = {
        val localView = LocalView.current
        val shadow = remember(localView) { ComposeShadow(localView, true) }
        DisposableEffect(localView) { onDispose { shadow.dispose() } }

        val useCompat = Build.VERSION.SDK_INT < 28 || forceCompatColor
        val ambient = if (useCompat) DefaultShadowColor else ambientColor
        val spot = if (useCompat) DefaultShadowColor else spotColor
        val filter = if (useCompat) compatColor else DefaultShadowColor

        drawBehind {
            with(shadow) { draw(elevation, shape, ambient, spot, filter) }
        }
    },
    inspectorInfo = debugInspectorInfo {
        name = "clippedShadow"
        properties["elevation"] = elevation
        properties["shape"] = shape
        properties["ambientColor"] = ambientColor
        properties["spotColor"] = spotColor
        properties["compatColor"] = compatColor
        properties["forceCompatColor"] = forceCompatColor
    }
)