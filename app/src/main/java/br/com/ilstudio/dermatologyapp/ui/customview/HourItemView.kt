package br.com.ilstudio.dermatologyapp.ui.customview

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.domain.model.AmPm
import br.com.ilstudio.dermatologyapp.domain.model.ItemHour

class HourItemView @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private val container: LinearLayout
    private val hour: TextView
    private val expedient: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_hour_item, this, true)
        container = findViewById(R.id.container)
        hour = findViewById(R.id.hour)
        expedient = findViewById(R.id.expedient)
    }

    fun bind(item: ItemHour, selected: Boolean) {
        hour.text = item.hour
        expedient.text = if (item.period == AmPm.AM) "AM" else "PM"
        setState(item.available, selected)
    }

    fun setOnButtonClickListener(listener: (View) -> Unit) {
        container.setOnClickListener(listener)
    }

    fun setState(available: Boolean, selected: Boolean) {
        when {
            !available -> showUnavailable()
            selected -> showSelected()
            else -> showAvailable()
        }
    }

    private fun showAvailable() {
        applyColors(R.color.secondary, R.color.black)
        container.isEnabled = true
        isSelected = false
    }

    private fun showUnavailable() {
        applyColors(R.color.blue_50, R.color.blue_200)
        container.isEnabled = false
        isSelected = false
    }

    private fun showSelected() {
        applyColors(R.color.primary, R.color.white)
        container.isEnabled = true
        isSelected = true
    }

    private fun applyColors(bgRes: Int, textColorRes: Int) {
        val bgColor = ContextCompat.getColor(context, bgRes)
        val txtColor = ContextCompat.getColor(context, textColorRes)
        container.backgroundTintList = ColorStateList.valueOf(bgColor)
        hour.setTextColor(txtColor)
        expedient.setTextColor(txtColor)
    }
}
