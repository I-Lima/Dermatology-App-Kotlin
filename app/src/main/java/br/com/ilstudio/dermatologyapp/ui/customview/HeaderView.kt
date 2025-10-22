package br.com.ilstudio.dermatologyapp.ui.customview

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
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
    private var button: ImageView
    var setOnBackButtonClickListener: (() -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_header, this, true)
        title = findViewById(R.id.title)
        button = findViewById(R.id.button_back)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.HeaderView, 0, 0)

            val labelText = typedArray.getString(R.styleable.HeaderView_title)
            title.text = labelText ?: ""

            val visibleButton = typedArray.getBoolean(R.styleable.HeaderView_headerButtonHidden, false)
            if (visibleButton) button.visibility = INVISIBLE
            else button.visibility = VISIBLE

            typedArray.recycle()
        }

        button.setOnClickListener {
            setOnBackButtonClickListener?.invoke() ?: (context as? Activity)?.finish()
        }

    }
}
