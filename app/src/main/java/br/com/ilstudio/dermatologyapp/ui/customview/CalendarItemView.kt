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
    }

    fun bind(item: CalendarItem) {
        date_number.text = item.date
        date_name.text = item.day
        setState(item)
    }

    fun setState(item: CalendarItem) {
        when {
            item.type == 1 -> showInactive()
            item.isSelected -> showSelected()
            else -> showAvailable()
        }
    }

    private fun showInactive() {
        applyColors(R.color.white, R.color.secondary)
        container.isActivated = false
        container.isClickable = false
    }

    private fun showAvailable() {
        applyColors(R.color.white, R.color.black)
        container.isActivated = false
        container.isClickable = false
    }

    private fun showSelected() {
        applyColors(R.color.primary, R.color.white)
        container.isActivated = true
        container.isClickable = true
    }

    private fun applyColors(bgRes: Int, textColorRes: Int) {
        val bgColor = getColor(context, bgRes)
        val txtColor = getColor(context, textColorRes)
        container.backgroundTintList = ColorStateList.valueOf(bgColor)
        date_number.setTextColor(txtColor)
        date_name.setTextColor(txtColor)
    }

    fun setOnButtonClickListener(listener: (View) -> Unit) {
        container.setOnClickListener(listener)
    }
}
