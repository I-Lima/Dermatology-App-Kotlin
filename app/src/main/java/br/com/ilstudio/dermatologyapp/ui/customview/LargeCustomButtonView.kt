package br.com.ilstudio.dermatologyapp.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import br.com.ilstudio.dermatologyapp.R

class LargeCustomButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {
    private var buttonView: Button
    private var progressBarView: ProgressBar
    private lateinit var labelText: String

    init {
        LayoutInflater.from(context).inflate(R.layout.view_large_custom_button, this, true)
        buttonView = findViewById(R.id.button)
        progressBarView = findViewById(R.id.loadingSpinner)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.LargeCustomButtonView, 0, 0)

            labelText = typedArray.getString(R.styleable.LargeCustomButtonView_labelTextButton) ?: ""
            buttonView.text = labelText


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

    fun showLoading(value: Boolean) {
        if (value) {
            buttonView.text = ""
            progressBarView.visibility = View.VISIBLE
        } else {
            buttonView.text = labelText
            progressBarView.visibility = View.GONE
        }
    }
}
