/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.style.ext

import android.util.DisplayMetrics
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import dev.icerock.moko.widgets.core.style.view.SizeSpec

fun SizeSpec.toPlatformSize(dm: DisplayMetrics): Int {
    return when (this) {
        SizeSpec.WrapContent -> ViewGroup.LayoutParams.WRAP_CONTENT
        SizeSpec.AsParent -> ViewGroup.LayoutParams.MATCH_PARENT
        is SizeSpec.Exact -> (this.points * dm.density).toInt()
        SizeSpec.MatchConstraint -> ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
    }
}
