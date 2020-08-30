/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.units

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.units.TableUnitItem
import platform.UIKit.UIActivityIndicatorView
import platform.UIKit.UITableView
import platform.UIKit.UITableViewCell
import platform.UIKit.UIView
import platform.UIKit.subviews

actual abstract class WidgetsTableUnitItem<T> actual constructor(
    override val itemId: Long,
    val data: T
) : TableUnitItem {
    actual abstract val reuseId: String
    actual abstract fun createWidget(data: LiveData<T>): UnitItemRoot

    override val reusableIdentifier: String get() = reuseId

    override fun register(intoView: UITableView) {
        intoView.registerClass(
            cellClass = UITableViewCell().`class`(),
            forCellReuseIdentifier = reusableIdentifier
        )
    }

    override fun bind(tableViewCell: UITableViewCell) {
        tableViewCell.contentView.setupWidgetContent(data) { createWidget(it).widget }
        findAnimated(tableViewCell.contentView).forEach { it.startAnimating() }
    }

    // temporary hack :(
    private fun findAnimated(view: UIView): List<UIActivityIndicatorView> {
        val subIndicators = view.subviews.flatMap { findAnimated(it as UIView) }
        return if (view is UIActivityIndicatorView) {
            subIndicators + view
        } else {
            subIndicators
        }
    }
}
