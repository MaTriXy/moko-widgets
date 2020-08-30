/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.widget

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.factory.SystemTextViewFactory
import dev.icerock.moko.widgets.core.style.view.WidgetSize

@WidgetDef(SystemTextViewFactory::class)
class TextWidget<WS : WidgetSize>(
    private val factory: ViewFactory<TextWidget<out WidgetSize>>,
    override val size: WS,
    override val id: Id?,
    val text: LiveData<StringDesc>,
    val maxLines: LiveData<Int?>? = null
) : Widget<WS>(), OptionalId<TextWidget.Id> {

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    interface Id : Theme.Id<TextWidget<out WidgetSize>>
    interface Category : Theme.Category<TextWidget<out WidgetSize>>

    object DefaultCategory : Category
}
