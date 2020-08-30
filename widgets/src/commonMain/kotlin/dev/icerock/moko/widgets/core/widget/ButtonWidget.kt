/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.widget

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.Image
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Value
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.factory.SystemButtonViewFactory
import dev.icerock.moko.widgets.core.style.view.WidgetSize

@WidgetDef(SystemButtonViewFactory::class)
class ButtonWidget<WS : WidgetSize>(
    private val factory: ViewFactory<ButtonWidget<out WidgetSize>>,
    override val size: WS,
    override val id: Id?,
    @Suppress("RemoveRedundantQualifierName")
    val content: ButtonWidget.Content,
    val enabled: LiveData<Boolean>?,
    val onTap: () -> Unit
) : Widget<WS>(), OptionalId<ButtonWidget.Id> {

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    sealed class Content {
        data class Text(val text: Value<StringDesc?>) : Content()
        data class Icon(val image: Value<Image>) : Content()
    }

    interface Id : Theme.Id<ButtonWidget<out WidgetSize>>
    interface Category : Theme.Category<ButtonWidget<out WidgetSize>>

    object DefaultCategory : Category
}
