@file:RequiresApi(Build.VERSION_CODES.LOLLIPOP)

package com.zedalpha.shadowgadgets.viewgroup

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.StackView
import androidx.annotation.RequiresApi

class ClippedShadowStackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.stackViewStyle,
    defStyleRes: Int = 0
) : StackView(context, attrs, defStyleAttr, defStyleRes), ClippedShadowsViewGroup {
    private val manager = ViewGroupShadowManager(this, attrs, true)

    override val isUsingShadowsFallback = manager.isUsingFallback

    override val clipAllChildShadows by manager::clipAllChildShadows
    override val childClippedShadowsPlane by manager::childClippedShadowsPlane
    override val childShadowsFallbackStrategy by manager::childShadowsFallbackStrategy

    override fun onViewAdded(child: View) {
        super.onViewAdded(child)
        manager.onViewAdded(child)
    }
}