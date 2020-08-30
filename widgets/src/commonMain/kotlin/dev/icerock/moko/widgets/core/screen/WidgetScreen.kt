/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.screen

import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.WidgetSize

expect abstract class WidgetScreen<Arg : Args>() : Screen<Arg> {
    abstract fun createContentWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>

    open val isKeyboardResizeContent: Boolean
    open val isDismissKeyboardOnTap: Boolean
    open val isScrollListOnKeyboardResize: Boolean
}
