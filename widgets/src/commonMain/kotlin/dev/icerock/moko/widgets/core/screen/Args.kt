/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.screen

import dev.icerock.moko.parcelize.Parcelable

sealed class Args {
    data class Parcel<T : Parcelable>(val args: T) : Args()
    object Empty : Args()
}