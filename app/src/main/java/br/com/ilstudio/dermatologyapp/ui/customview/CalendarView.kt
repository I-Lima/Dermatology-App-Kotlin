package br.com.ilstudio.dermatologyapp.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.adapter.CalendarItemAdapter
import br.com.ilstudio.dermatologyapp.domain.model.CalendarItem
import br.com.ilstudio.dermatologyapp.utils.AdapterLayout
import br.com.ilstudio.dermatologyapp.utils.DateUtils

class CalendarView @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {
    private var backButton: ImageView
    private var nextButton: ImageView
    private var recyclerView: RecyclerView
    private var listener: ((CalendarItem) -> Unit)? = null
    private var items: List<CalendarItem>

    init {
        LayoutInflater.from(context).inflate(R.layout.view_calendar, this, true)
        backButton = findViewById(R.id.back_button_component)
        nextButton = findViewById(R.id.next_button_component)
        recyclerView = findViewById(R.id.day_recycle)

        items = DateUtils.getCurrentWeekDays()
        val adapter = CalendarItemAdapter(items) { item ->
            listener?.invoke(item)
        }
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.addItemDecoration(AdapterLayout.HorizontalSpaceItemDecoration(12))
        recyclerView.adapter = adapter

        backButton.setOnClickListener {
            items = DateUtils.subtractWeekDays(items[0].searchDate)
            adapter.updateItems(items)
        }

        nextButton.setOnClickListener {
            items = DateUtils.addWeekDays(items[4].searchDate)
            adapter.updateItems(items)
        }
    }

    fun setOnDayClickListener(listener: (CalendarItem) -> Unit) {
        this.listener = listener
    }
}
