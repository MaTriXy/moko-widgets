/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.permissions

import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.widgets.core.screen.Screen

expect fun Screen<*>.createPermissionsController(): PermissionsController

expect fun PermissionsController.bind(screen: Screen<*>)
