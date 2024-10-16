package br.com.ilstudio.dermatologyapp.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import br.com.ilstudio.dermatologyapp.R

class MenuItemProfileView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs,defStyleAttr) {
    private var imageView: ImageView
    private var textView: TextView
    private var button: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_menu_item_profile, this, true)
        imageView = findViewById(R.id.img)
        textView = findViewById(R.id.text)
        button = findViewById(R.id.button)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.MenuItemProfileView, 0, 0)

            val iconResId = typedArray.getResourceId(R.styleable.MenuItemProfileView_iconSrc, -1)
            if (iconResId != -1) {
                imageView.setImageResource(iconResId)
            }

            val labelText = typedArray.getString(R.styleable.MenuItemProfileView_labelText)
            textView.text = labelText ?: ""

            val hidden = typedArray.getBoolean(R.styleable.MenuItemProfileView_buttonHidden, false)
            if (hidden) button.visibility = INVISIBLE
            else button.visibility = VISIBLE

            typedArray.recycle()
        }

    }

}
