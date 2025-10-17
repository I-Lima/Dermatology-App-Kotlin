package br.com.ilstudio.dermatologyapp.ui.customview

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import br.com.ilstudio.dermatologyapp.R

class ButtonTagView @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
): ConstraintLayout(context, attrs) {
    private var container: LinearLayout
    private var title: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_button_tag, this, true)
        container = findViewById(R.id.container)
        title = findViewById(R.id.title)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ButtonTagView, 0, 0)

            val labelTitle = typedArray.getString(R.styleable.ButtonTagView_titleTag)
            title.text = labelTitle ?: ""

            val type = typedArray.getString(R.styleable.ButtonTagView_typeTag) ?: "active"
            when(type) {
                "0" -> {
                    setTypeTag(false)
                }
                "1" -> {
                    setTypeTag(true)
                }
            }

            typedArray.recycle()
        }
    }

    fun setOnButtonClickListener(listener: (View) -> Unit) {
        container.setOnClickListener(listener)
    }

    fun setTypeTag(activated: Boolean) {
        if(activated) {
            container.backgroundTintList = ColorStateList.valueOf(getColor(context, R.color.primary))
            title.setTextColor(getColor(context, R.color.white))
        } else {
            container.backgroundTintList = null
            container.background = getDrawable(context, R.drawable.shape_view_button_tag_inactive)
            title.setTextColor(getColor(context, R.color.blue_200))
        }
    }

}
