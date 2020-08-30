/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import dev.icerock.moko.widgets.core.style.view.WidgetSize

interface OptionalId<T : Theme.Id<out Widget<out WidgetSize>>> {
    val id: T?
}
