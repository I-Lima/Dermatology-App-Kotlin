package br.com.ilstudio.dermatologyapp.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import br.com.ilstudio.dermatologyapp.R

class LargeCustomButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {
    private var buttonView: Button

    init {
        LayoutInflater.from(context).inflate(R.layout.view_large_custom_button, this, true)
        buttonView = findViewById(R.id.button)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.LargeCustomButtonView, 0, 0)

            val labelText = typedArray.getString(R.styleable.LargeCustomButtonView_labelTextButton)
            buttonView.text = labelText ?: ""


            val enabledString = typedArray.getBoolean(R.styleable.LargeCustomButtonView_active, true)
            setActive(enabledString)

            typedArray.recycle()
        }
    }

    fun setOnButtonClickListener(listener: (View) -> Unit) {
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener(listener)
    }

    fun setActive(isActived: Boolean) {
        buttonView.isEnabled = isActived
    }
}
