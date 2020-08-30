/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import android.widget.FrameLayout
import android.widget.ScrollView
import dev.icerock.moko.widgets.core.widget.ScrollWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.style.applyPaddingIfNeeded
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.ext.applyMargin
import dev.icerock.moko.widgets.core.style.ext.toPlatformSize
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.androidId

actual class SystemScrollViewFactory actual constructor(
    private val background: Background<Fill.Solid>?,
    private val padding: PaddingValues?,
    private val margins: MarginValues?
) : ViewFactory<ScrollWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: ScrollWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext
        val lifecycleOwner = viewFactoryContext.lifecycleOwner
        val dm = context.resources.displayMetrics

        val scrollView = ScrollView(context).apply {
            applyBackgroundIfNeeded(this@SystemScrollViewFactory.background)
            applyPaddingIfNeeded(padding)

            id = widget.id.androidId
        }

        val childBundle = widget.child.buildView(
            ViewFactoryContext(
                context = context,
                lifecycleOwner = lifecycleOwner,
                parent = scrollView
            )
        )
        val childView = childBundle.view
        val childSize = childBundle.size
        val childMargins = childBundle.margins

        scrollView.addView(
            childView,
            FrameLayout.LayoutParams(
                childSize.width.toPlatformSize(dm),
                childSize.height.toPlatformSize(dm)
            ).apply {
                childMargins?.let { applyMargin(dm, it) }
            }
        )

        return ViewBundle(
            view = scrollView,
            size = size,
            margins = margins
        )
    }
}
