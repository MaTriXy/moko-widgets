/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import dev.icerock.moko.widgets.core.widget.LinearWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.style.applyPaddingIfNeeded
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.ext.applyMargin
import dev.icerock.moko.widgets.core.style.ext.toLinearLayoutOrientation
import dev.icerock.moko.widgets.core.style.ext.toPlatformSize
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.view.AspectRatioFrameLayout

actual class LinearViewFactory actual constructor(
    private val padding: PaddingValues?,
    private val margins: MarginValues?,
    private val background: Background<out Fill>?
) : ViewFactory<LinearWidget<out WidgetSize>> {
    override fun <WS : WidgetSize> build(
        widget: LinearWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.context
        val dm = context.resources.displayMetrics

        val container = LinearLayout(context).apply {
            orientation = widget.orientation.toLinearLayoutOrientation()

            applyPaddingIfNeeded(padding)
            applyBackgroundIfNeeded(this@LinearViewFactory.background)
        }

        widget.children.forEach { child ->
            val viewBundle = child.buildView(
                ViewFactoryContext(
                    context = context,
                    lifecycleOwner = viewFactoryContext.lifecycleOwner,
                    parent = container
                )
            )
            val (lp, view) = (viewBundle.size to viewBundle.view)
                .toLinearLayoutParams(dm)

            viewBundle.margins?.let { lp.applyMargin(dm, it) }

            container.addView(view, lp)
        }

        return ViewBundle(
            view = container,
            size = size,
            margins = margins
        )
    }

    private fun Pair<WidgetSize, View>.toLinearLayoutParams(
        dm: DisplayMetrics
    ): Pair<LinearLayout.LayoutParams, View> {
        val widgetSize = this.first
        val view = this.second
        return when (widgetSize) {
            is WidgetSize.Const<out SizeSpec, out SizeSpec> -> {
                LinearLayout.LayoutParams(
                    widgetSize.width.toPlatformSize(dm),
                    widgetSize.height.toPlatformSize(dm)
                ) to view
            }
            is WidgetSize.AspectByWidth<out SizeSpec> -> {
                val lp = LinearLayout.LayoutParams(
                    widgetSize.width.toPlatformSize(dm),
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                lp to AspectRatioFrameLayout(
                    context = view.context,
                    aspectRatio = widgetSize.aspectRatio,
                    aspectByWidth = true
                ).apply {
                    addView(
                        view,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            }
            is WidgetSize.AspectByHeight<out SizeSpec> -> {
                val lp = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    widgetSize.height.toPlatformSize(dm)
                )

                lp to AspectRatioFrameLayout(
                    context = view.context,
                    aspectRatio = widgetSize.aspectRatio,
                    aspectByWidth = false
                ).apply {
                    addView(
                        view,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            }
        }
    }
}
