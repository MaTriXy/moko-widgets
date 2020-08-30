/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.style

import android.view.View
import android.view.ViewGroup
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.background.buildBackground
import dev.icerock.moko.widgets.core.style.ext.applyPadding
import dev.icerock.moko.widgets.core.style.ext.toPlatformSize
import dev.icerock.moko.widgets.core.style.state.PressableState
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.view.AspectRatioFrameLayout

fun View.applyBackgroundIfNeeded(background: Background<out Fill>?) {
    if (background == null) return

    this.background = background.buildBackground(context)
}

fun View.applyStateBackgroundIfNeeded(stateBackground: PressableState<Background<out Fill>>?) {
    if (stateBackground == null) return

    this.background = stateBackground.buildBackground(context)
}

fun View.applyPaddingIfNeeded(padding: PaddingValues?) {
    if (padding == null) return

    applyPadding(padding)
}

fun View.withSize(size: WidgetSize): View {
    val dm = context.resources.displayMetrics

    val aspectRatioFrameLayout = when (size) {
        is WidgetSize.Const<out SizeSpec, out SizeSpec> -> {
            layoutParams = ViewGroup.MarginLayoutParams(
                size.width.toPlatformSize(dm),
                size.height.toPlatformSize(dm)
            )
            return this
        }
        is WidgetSize.AspectByWidth<out SizeSpec> -> {
            AspectRatioFrameLayout(
                context = context,
                aspectRatio = size.aspectRatio,
                aspectByWidth = true
            ).apply {
                layoutParams = ViewGroup.MarginLayoutParams(
                    size.width.toPlatformSize(dm),
                    0
                )
            }
        }
        is WidgetSize.AspectByHeight<out SizeSpec> -> {
            AspectRatioFrameLayout(
                context = context,
                aspectRatio = size.aspectRatio,
                aspectByWidth = false
            ).apply {
                layoutParams = ViewGroup.MarginLayoutParams(
                    0,
                    size.height.toPlatformSize(dm)
                )
            }
        }
    }

    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    aspectRatioFrameLayout.addView(this)

    return aspectRatioFrameLayout
}
