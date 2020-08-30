/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.widgets.core.widget.ListWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.style.view.WidgetSize

expect class AutoScrollListViewFactory(
    listViewFactory: ViewFactory<ListWidget<out WidgetSize>>,
    isAlwaysAutoScroll: Boolean = false
) : ViewFactory<ListWidget<out WidgetSize>>