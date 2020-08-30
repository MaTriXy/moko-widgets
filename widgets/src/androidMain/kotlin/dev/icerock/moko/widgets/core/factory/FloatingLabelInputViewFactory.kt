/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.MarginLayoutParamsCompat
import com.google.android.material.internal.CollapsingTextHelper
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.core.R
import dev.icerock.moko.widgets.core.widget.InputWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.style.applyPaddingIfNeeded
import dev.icerock.moko.widgets.core.style.applyTextStyleIfNeeded
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.ext.getGravity
import dev.icerock.moko.widgets.core.style.state.FocusableState
import dev.icerock.moko.widgets.core.style.view.FontStyle
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.TextHorizontalAlignment
import dev.icerock.moko.widgets.core.style.view.TextStyle
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.androidId
import dev.icerock.moko.widgets.core.utils.bind
import dev.icerock.moko.widgets.core.utils.dp
import dev.icerock.moko.widgets.core.utils.sp

actual class FloatingLabelInputViewFactory actual constructor(
    private val background: Background<Fill.Solid>?,
    private val margins: MarginValues?,
    private val padding: PaddingValues?,
    private val textStyle: TextStyle<Color>?,
    private val labelTextStyle: TextStyle<Color>?,
    private val errorTextStyle: TextStyle<Color>?,
    private val underLineColor: FocusableState<Color>?,
    private val textHorizontalAlignment: TextHorizontalAlignment?
) : ViewFactory<InputWidget<out WidgetSize>> {

    @SuppressLint("RestrictedApi")
    override fun <WS : WidgetSize> build(
        widget: InputWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext
        val lifecycleOwner = viewFactoryContext.lifecycleOwner

        val textInputLayout = TextInputLayout(context)
        val collapsingTextHelper = textInputLayout.getCollapsingTextHelper()

        textInputLayout.applyBackgroundIfNeeded(background)
        textInputLayout.applyPaddingIfNeeded(padding)

        val editText = TextInputEditText(context).apply {
            id = widget.id.androidId

            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                // EditText's default background have paddings 4dp, while we not change background to own we just change margins
                // https://stackoverflow.com/questions/31735291/removing-the-left-padding-on-an-android-edittext/44497551
                val dp4 = (-4).dp(context)
                MarginLayoutParamsCompat.setMarginStart(this, dp4)
                MarginLayoutParamsCompat.setMarginEnd(this, dp4)
            }

            applyTextStyleIfNeeded(textStyle)
            widget.inputType?.applyTo(this)

            this@FloatingLabelInputViewFactory.textHorizontalAlignment?.let {
                gravity = it.getGravity()
            }

            underLineColor?.also { stateColor ->
                supportBackgroundTintList = ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_focused, -android.R.attr.state_pressed),
                        intArrayOf()
                    ),
                    intArrayOf(
                        stateColor.unfocused.argb.toInt(),
                        stateColor.focused.argb.toInt()
                    )
                )
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) widget.field.validate()
            }
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s == null) return

                    widget.field.data.value = s.toString()
                }
            })
        }

        textInputLayout.addView(editText)

        labelTextStyle?.also {
            if (it.color != null) {
                val hintColor = ColorStateList.valueOf(it.color.argb.toInt())
                textInputLayout.defaultHintTextColor = hintColor
            }
            if (it.size != null) {
                collapsingTextHelper.collapsedTextSize = it.size.toFloat().sp(context)
            }
            if (it.fontStyle != null) {
                collapsingTextHelper.collapsedTypeface = when (it.fontStyle) {
                    FontStyle.BOLD -> Typeface.DEFAULT_BOLD
                    FontStyle.MEDIUM -> Typeface.DEFAULT
                    FontStyle.ITALIC -> Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
                }
                collapsingTextHelper.expandedTypeface = collapsingTextHelper.collapsedTypeface
            }
        }

        widget.field.data.bind(lifecycleOwner) { data ->
            if (editText.text?.toString() == data) return@bind

            editText.setText(data)
        }
        widget.field.error.bind(lifecycleOwner) { error ->
            textInputLayout.error = error?.toString(context)
            textInputLayout.isErrorEnabled = error != null

            if (textInputLayout.isErrorEnabled) {
                val errorText = textInputLayout.findViewById<TextView>(R.id.textinput_error)
                errorText.applyTextStyleIfNeeded(errorTextStyle)
            }
        }

        widget.label.bind(lifecycleOwner) { textInputLayout.hint = it?.toString(context) }
        widget.enabled?.bind(lifecycleOwner) { editText.isEnabled = it == true }
        widget.maxLines?.bind(lifecycleOwner) { maxLines ->
            when (maxLines) {
                null -> editText.setSingleLine(false)
                1 -> editText.setSingleLine(true)
                else -> {
                    editText.setSingleLine(false)
                    editText.maxLines = maxLines
                }
            }
        }

        return ViewBundle(
            view = textInputLayout,
            size = size,
            margins = margins
        )
    }

    private fun TextInputLayout.getCollapsingTextHelper(): CollapsingTextHelper {
        val clazz = TextInputLayout::class.java
        val field = clazz.getDeclaredField("collapsingTextHelper")
        field.isAccessible = true
        return field.get(this) as CollapsingTextHelper
    }

}
