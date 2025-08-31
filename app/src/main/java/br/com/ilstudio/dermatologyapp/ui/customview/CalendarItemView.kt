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
import br.com.ilstudio.dermatologyapp.domain.model.CalendarItem

class CalendarItemView @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
): ConstraintLayout(context, attrs) {
    private var date_number: TextView
    private var date_name: TextView
    private var container: LinearLayout

    init {
        LayoutInflater.from(context).inflate(R.layout.view_calendar_item, this, true)
        date_number = findViewById(R.id.date_number)
        date_name = findViewById(R.id.date_name)
        container = findViewById(R.id.container)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CalendarItemView, 0, 0)
            date_number.text = typedArray.getString(R.styleable.CalendarItemView_day_number) ?: ""
            date_name.text   = typedArray.getString(R.styleable.CalendarItemView_day_name) ?: ""
            val type = typedArray.getInt(R.styleable.CalendarItemView_type, 0)

            when (type) {
                0 -> {
                    container.backgroundTintList = ColorStateList.valueOf(getColor(context, R.color.white))
                    date_number.setTextColor(getColor(context, R.color.black))
                    date_name.setTextColor(getColor(context, R.color.black))
                }
                1 -> {
                    container.backgroundTintList = ColorStateList.valueOf(getColor(context, R.color.white))
                    date_number.setTextColor(getColor(context, R.color.secondary))
                    date_name.setTextColor(getColor(context, R.color.secondary))
                    container.isActivated = false
                }
                2 -> {
                    container.backgroundTintList = ColorStateList.valueOf(getColor(context, R.color.primary))
                    date_number.setTextColor(getColor(context, R.color.white))
                    date_name.setTextColor(getColor(context, R.color.white))
                }
            }
            typedArray.recycle()
        }
    }

    fun bind(item: CalendarItem) {
        date_number.text = item.date
        date_name.text = item.day

        when(item.type) {
            0 -> {
                container.backgroundTintList = ColorStateList.valueOf(getColor(context, R.color.white))
                date_number.setTextColor(getColor(context, R.color.black))
                date_name.setTextColor(getColor(context, R.color.black))
            }
            1 -> {
                container.backgroundTintList = ColorStateList.valueOf(getColor(context, R.color.white))
                date_number.setTextColor(getColor(context, R.color.secondary))
                date_name.setTextColor(getColor(context, R.color.secondary))
                container.isActivated = false
            }
            2 -> {
                container.backgroundTintList = ColorStateList.valueOf(getColor(context, R.color.primary))
                date_number.setTextColor(getColor(context, R.color.white))
                date_name.setTextColor(getColor(context, R.color.white))
            }
        }
    }

    fun setOnButtonClickListener(listener: (View) -> Unit) {
        container.setOnClickListener(listener)
    }
}
