/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.universal

import com.icerockdev.library.SharedFactory
import com.icerockdev.library.sample.CryptoProfileScreen
import com.icerockdev.library.sample.CryptoProfileViewModel
import com.icerockdev.library.sample.McommerceProfileScreen
import com.icerockdev.library.sample.McommerceProfileViewModel
import com.icerockdev.library.sample.PostsScreen
import com.icerockdev.library.sample.PostsViewModel
import com.icerockdev.library.sample.SocialProfileScreen
import com.icerockdev.library.sample.SocialProfileViewModel
import com.icerockdev.library.sample.StateScreen
import com.icerockdev.library.sample.StateViewModel
import com.icerockdev.library.sample.UsersScreen
import com.icerockdev.library.sample.UsersViewModel
import dev.icerock.moko.widgets.collection.CollectionWidget
import dev.icerock.moko.widgets.core.widget.constraint
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.screen.Args
import dev.icerock.moko.widgets.core.screen.WidgetScreen
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.widget.tabs

class WidgetsScreen(
    private val sharedFactory: SharedFactory,
    private val theme: Theme,
    private val postsCollectionCategory: CollectionWidget.Category
) : WidgetScreen<Args.Empty>() {

    override fun createContentWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> {
        return with(theme) {
            constraint(size = WidgetSize.AsParent) {
                val tabs = +tabs(
                    size = WidgetSize.Const(
                        width = SizeSpec.MatchConstraint,
                        height = SizeSpec.MatchConstraint
                    )
                ) {
                    tab(
                        title = const("P#2"),
                        body = SocialProfileScreen(
                            theme = theme, //AppTheme.socialWidgetScope,
                            viewModel = SocialProfileViewModel()
                        ).createWidget()
                    )
                    tab(
                        title = const("P#4"),
                        body = CryptoProfileScreen(
                            theme = theme, //AppTheme.cryptoWidgetScope,
                            viewModel = CryptoProfileViewModel()
                        ).createWidget()
                    )
                    tab(
                        title = const("P#1"),
                        body = SocialProfileScreen(
                            theme = theme,
                            viewModel = SocialProfileViewModel()
                        ).createWidget()
                    )
                    tab(
                        title = const("P#3"),
                        body = McommerceProfileScreen(
                            theme = theme, //AppTheme.mcommerceWidgetScope,
                            viewModel = McommerceProfileViewModel()
                        ).createWidget()
                    )
                    tab(
                        title = const("D"),
                        body = StateScreen(
                            theme = theme,
                            viewModel = StateViewModel()
                        ).createWidget()
                    )
                    tab(
                        title = const("P"),
                        body = PostsScreen(
                            theme = theme,
                            viewModel = PostsViewModel()
                        ).createWidget()
                    )
                    tab(
                        title = const("U"),
                        body = UsersScreen(
                            theme = theme,
                            viewModel = UsersViewModel(sharedFactory.usersUnitsFactory)
                        ).createWidget()
                    )
                }

                constraints {
                    tabs topToTop root.safeArea
                    tabs bottomToBottom root.safeArea
                    tabs leftRightToLeftRight root
                }
            }
        }
    }
}
