/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.bottomsheet

import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.screen.Screen
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.WidgetSize

expect fun Screen<*>.showBottomSheet(
    content: Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.WrapContent>>,
    onDismiss: (isSelfDismissed: Boolean) -> Unit
): SelfDismisser?

interface SelfDismisser {
    fun dismissSelf()
}
