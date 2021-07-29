/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.permissions

import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.widgets.core.screen.Screen
import dev.icerock.moko.permissions.ios.PermissionsController as IosPermissionsController

actual fun Screen<*>.createPermissionsController(): PermissionsController {
    return IosPermissionsController()
}

actual fun PermissionsController.bind(screen: Screen<*>) {
    // do nothing - bind needed only for android
}
