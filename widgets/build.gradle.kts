/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("com.android.library")
    id("dev.icerock.mobile.multiplatform-widgets-generator")
    id("org.jetbrains.kotlin.multiplatform")
    id("kotlin-kapt")
    id("kotlin-android-extensions")
    id("dev.icerock.mobile.multiplatform")
    id("maven-publish")
}

dependencies {
    mppLibrary(Deps.Libs.MultiPlatform.kotlinStdLib)

    mppLibrary(Deps.Libs.MultiPlatform.coroutines)

    mppLibrary(Deps.Libs.MultiPlatform.mokoResources)
    mppLibrary(Deps.Libs.MultiPlatform.mokoMvvm)
    mppLibrary(Deps.Libs.MultiPlatform.mokoFields)
    mppLibrary(Deps.Libs.MultiPlatform.mokoUnits)
    mppLibrary(Deps.Libs.MultiPlatform.mokoGraphics)
    mppLibrary(Deps.Libs.MultiPlatform.mokoParcelize)

    androidLibrary(Deps.Libs.Android.appCompat)
    androidLibrary(Deps.Libs.Android.fragment)
    androidLibrary(Deps.Libs.Android.recyclerView)
    androidLibrary(Deps.Libs.Android.material)
    androidLibrary(Deps.Libs.Android.swipeRefreshLayout)
    androidLibrary(Deps.Libs.Android.constraintLayout)
    androidLibrary(Deps.Libs.Android.inputMask)
    androidLibrary(Deps.Libs.Android.roundedImageView)
}

kotlin {
    targets.filterIsInstance<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().forEach { target ->
        target.compilations.getByName("main") {
            val objcAddtition by cinterops.creating {
                defFile(project.file("src/iosMain/def/objcAddtition.def"))
                packageName("dev.icerock.moko.widgets.core.objc")
            }
        }
    }
}
