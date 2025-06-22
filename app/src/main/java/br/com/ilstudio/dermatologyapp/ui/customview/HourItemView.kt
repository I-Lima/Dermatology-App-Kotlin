package br.com.ilstudio.dermatologyapp.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.ilstudio.dermatologyapp.R

class HourItemView @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
): ConstraintLayout(context, attrs) {
    private var hour: TextView
    private var expedient: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_hour_item, this, true)
        hour = findViewById(R.id.hour)
        expedient = findViewById(R.id.expedient)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.HourItemView, 0, 0)

            val labelHour = typedArray.getString(R.styleable.HourItemView_hour)
            val labelExp = typedArray.getString(R.styleable.HourItemView_expedient)
            hour.text = labelHour ?: ""
            expedient.text = labelExp ?: ""

            typedArray.recycle()
        }
    }

    fun setOnButtonClickListener(listener: (View) -> Unit) {
        val button = findViewById<LinearLayout>(R.id.main)
        button.setOnClickListener(listener)
    }
}