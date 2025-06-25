package br.com.ilstudio.dermatologyapp.ui.customview

import android.content.Context
import android.content.res.ColorStateList
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.ilstudio.dermatologyapp.R

class InputView @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
): ConstraintLayout(context, attrs) {
    private var title: TextView
    private var input: EditText

    init {
        LayoutInflater.from(context).inflate(R.layout.view_input, this, true)
        title = findViewById(R.id.title)
        input = findViewById(R.id.input)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.InputView, 0, 0)

            val labelTitle = typedArray.getString(R.styleable.InputView_titleInput)
            title.text = labelTitle ?: ""

            val labelPlace = typedArray.getString(R.styleable.InputView_placeholder)
            input.hint = labelPlace ?: ""

            val type = typedArray.getString(R.styleable.InputView_typeInput) ?: "text"
            when(type) {
                "0" -> { input.inputType = InputType.TYPE_CLASS_TEXT }
                "1" -> { input.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD }
                "2" -> { input.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS }
                "3" -> { input.inputType = InputType.TYPE_CLASS_PHONE }
                "4" -> { input.inputType = InputType.TYPE_CLASS_NUMBER }
            }

            typedArray.recycle()
        }
    }
}