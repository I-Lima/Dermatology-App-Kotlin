package br.com.ilstudio.dermatologyapp.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.ilstudio.dermatologyapp.R

class HeaderView @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {
    private var title: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_header, this, true)
        title = findViewById(R.id.title)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.HeaderView, 0, 0)

            val labelText = typedArray.getString(R.styleable.HeaderView_title)
            title.text = labelText ?: ""

            typedArray.recycle()
        }
    }
    fun setOnBackButtonClickListener(listener: (View) -> Unit) {
        val buttonBack = findViewById<ImageView>(R.id.button_back)
        buttonBack.setOnClickListener(listener)
    }
}
