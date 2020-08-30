/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.widget

import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.factory.ConstraintViewFactory
import dev.icerock.moko.widgets.core.style.view.WidgetSize

@WidgetDef(ConstraintViewFactory::class)
class ConstraintWidget<WS : WidgetSize>(
    private val factory: ViewFactory<ConstraintWidget<out WidgetSize>>,
    override val size: WS,
    override val id: Id?,
    @Suppress("RemoveRedundantQualifierName")
    builder: ConstraintWidget.ChildrenBuilder.() -> ConstraintsApi.() -> Unit
) : Widget<WS>(), OptionalId<ConstraintWidget.Id> {

    val children: List<Widget<out WidgetSize>>
    val constraints: ConstraintsApi.() -> Unit

    init {
        val cb = ChildrenBuilder()
        constraints = builder(cb)
        children = cb.build()
    }

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    class ChildrenBuilder internal constructor() {
        private val children: MutableList<Widget<out WidgetSize>> = mutableListOf()

        val root: ConstraintItem.Root = ConstraintItem.Root

        operator fun Widget<out WidgetSize>.unaryPlus(): ConstraintItem.Child {
            children.add(this)
            return ConstraintItem.Child(this)
        }

        fun constraints(block: ConstraintsApi.() -> Unit): ConstraintsApi.() -> Unit {
            return block
        }

        fun build(): List<Widget<out WidgetSize>> {
            return children
        }
    }

    interface Id : Theme.Id<ConstraintWidget<out WidgetSize>>
    interface Category : Theme.Category<ConstraintWidget<out WidgetSize>>

    object DefaultCategory : Category
}

sealed class ConstraintItem {
    object Root : ConstraintItem() {
        val safeArea = SafeArea(from = this)
    }

    data class Child(val widget: Widget<out WidgetSize>) : ConstraintItem()
    data class SafeArea(val from: Root) : ConstraintItem()

    val top get() = VerticalAnchor(item = this, edge = VerticalAnchor.Edge.TOP)
    val bottom get() = VerticalAnchor(item = this, edge = VerticalAnchor.Edge.BOTTOM)

    val left get() = HorizontalAnchor(item = this, edge = HorizontalAnchor.Edge.LEFT)
    val right get() = HorizontalAnchor(item = this, edge = HorizontalAnchor.Edge.RIGHT)

    class VerticalAnchor(val item: ConstraintItem, val edge: Edge) {
        enum class Edge {
            TOP,
            BOTTOM
        }
    }

    class HorizontalAnchor(val item: ConstraintItem, val edge: Edge) {
        enum class Edge {
            LEFT,
            RIGHT
        }
    }
}

interface Constraint {
    infix fun offset(points: Int)
}

// TODO rework for allow extension from outside
@Suppress("TooManyFunctions")
interface ConstraintsApi {
    infix fun ConstraintItem.Child.leftToRight(to: ConstraintItem): Constraint
    infix fun ConstraintItem.Child.leftToLeft(to: ConstraintItem): Constraint
    infix fun ConstraintItem.Child.rightToRight(to: ConstraintItem): Constraint
    infix fun ConstraintItem.Child.rightToLeft(to: ConstraintItem): Constraint
    infix fun ConstraintItem.Child.topToTop(to: ConstraintItem): Constraint
    infix fun ConstraintItem.Child.topToBottom(to: ConstraintItem): Constraint
    infix fun ConstraintItem.Child.centerYToCenterY(to: ConstraintItem)
    infix fun ConstraintItem.Child.centerXToCenterX(to: ConstraintItem)
    infix fun ConstraintItem.Child.bottomToBottom(to: ConstraintItem): Constraint
    infix fun ConstraintItem.Child.bottomToTop(to: ConstraintItem): Constraint

    infix fun ConstraintItem.Child.leftRightToLeftRight(to: ConstraintItem): Constraint {
        val lc = this leftToLeft to
        val rc = this rightToRight to

        return object : Constraint {
            override fun offset(points: Int) {
                lc.offset(points)
                rc.offset(points)
            }
        }
    }

    fun ConstraintItem.Child.verticalCenterBetween(
        top: ConstraintItem.VerticalAnchor,
        bottom: ConstraintItem.VerticalAnchor
    )

    fun ConstraintItem.Child.horizontalCenterBetween(
        left: ConstraintItem.HorizontalAnchor,
        right: ConstraintItem.HorizontalAnchor
    )

    infix fun ConstraintItem.VerticalAnchor.pin(to: ConstraintItem.VerticalAnchor): Constraint
    infix fun ConstraintItem.HorizontalAnchor.pin(to: ConstraintItem.HorizontalAnchor): Constraint
}
