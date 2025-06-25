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
import br.com.ilstudio.dermatologyapp.R

class HourItemView @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
): ConstraintLayout(context, attrs) {
    private var container: LinearLayout
    private var hour: TextView
    private var expedient: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_hour_item, this, true)
        container = findViewById(R.id.container)
        hour = findViewById(R.id.hour)
        expedient = findViewById(R.id.expedient)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.HourItemView, 0, 0)

            val labelHour = typedArray.getString(R.styleable.HourItemView_hour)
            hour.text = labelHour ?: ""

            val exped = typedArray.getString(R.styleable.HourItemView_expedient) ?: "am"
            when(exped) {
                "0" -> {
                    expedient.text = "AM"
                }
                "1" -> {
                    expedient.text = "PM"
                }
            }

            val type = typedArray.getString(R.styleable.HourItemView_typeTime) ?: "active"
            when(type) {
                "0" -> {
                    container.backgroundTintList = ColorStateList.valueOf(getColor(context,R.color.secondary))
                    hour.setTextColor(getColor(context,R.color.black))
                    expedient.setTextColor(getColor(context,R.color.black))
                }
                "1" -> {
                    container.backgroundTintList = ColorStateList.valueOf(getColor(context,R.color.blue_50))
                    hour.setTextColor(getColor(context,R.color.blue_200))
                    expedient.setTextColor(getColor(context,R.color.blue_200))
                    container.isActivated = false
                }
                "2" -> {
                    container.backgroundTintList = ColorStateList.valueOf(getColor(context,R.color.primary))
                    hour.setTextColor(getColor(context,R.color.white))
                    expedient.setTextColor(getColor(context,R.color.white))
                }
            }

            typedArray.recycle()
        }
    }

    fun setOnButtonClickListener(listener: (View) -> Unit) {
        val button = findViewById<LinearLayout>(R.id.main)
        button.setOnClickListener(listener)
    }
}