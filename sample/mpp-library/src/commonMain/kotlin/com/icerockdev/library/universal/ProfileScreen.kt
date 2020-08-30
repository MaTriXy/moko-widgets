/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.universal

import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcherOwner
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.core.screen.Args
import dev.icerock.moko.widgets.core.screen.Screen
import dev.icerock.moko.widgets.core.screen.getArgument
import dev.icerock.moko.widgets.core.screen.getViewModel
import dev.icerock.moko.widgets.core.screen.listen
import dev.icerock.moko.widgets.core.screen.navigation.NavigationBar
import dev.icerock.moko.widgets.core.screen.navigation.NavigationItem
import dev.icerock.moko.widgets.core.screen.navigation.Route
import dev.icerock.moko.widgets.core.screen.showToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class ProfileScreen : Screen<Args.Parcel<ProfileScreen.Arg>>(),
    ProfileViewModel.EventsListener, NavigationItem {

    protected abstract val backRoute: Route<Unit>

    override val navigationBar: NavigationBar = NavigationBar.Normal(title = "Profile".desc())

    protected val profileViewModel: ProfileViewModel by lazy {
        val arg = getArgument()
        val viewModel = getViewModel {
            ProfileViewModel(
                eventsDispatcher = createEventsDispatcher(),
                userId = arg.userId
            ).apply { start() }
        }
        viewModel.eventsDispatcher.listen(this, this)
        return@lazy viewModel
    }

    override fun showTimedMessage(message: StringDesc) {
        showToast(message)
    }

    @Parcelize
    data class Arg(
        val userId: Int
    ) : Parcelable
}

expect class PlatformProfileScreen(backRoute: Route<Unit>) : ProfileScreen

class ProfileViewModel(
    override val eventsDispatcher: EventsDispatcher<EventsListener>,
    val userId: Int
) : ViewModel(), EventsDispatcherOwner<ProfileViewModel.EventsListener> {

    val text: LiveData<StringDesc> = MutableLiveData(initialValue = "User $userId".desc())

    fun start() {
        viewModelScope.launch {
            delay(2000)

            val message = "Hello Profile $userId".desc()
            eventsDispatcher.dispatchEvent { showTimedMessage(message) }
        }
    }

    interface EventsListener {
        fun showTimedMessage(message: StringDesc)
    }
}
