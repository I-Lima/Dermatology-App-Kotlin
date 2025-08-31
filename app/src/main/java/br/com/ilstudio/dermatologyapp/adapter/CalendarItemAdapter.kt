package br.com.ilstudio.dermatologyapp.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ilstudio.dermatologyapp.domain.model.CalendarItem
import br.com.ilstudio.dermatologyapp.ui.customview.CalendarItemView

class CalendarItemAdapter(
    private val items: List<CalendarItem>,
    private val onItemClick: (CalendarItem) -> Unit
) : RecyclerView.Adapter<CalendarItemAdapter.MyViewHolder>() {

    class MyViewHolder(private val view: CalendarItemView) : RecyclerView.ViewHolder(view) {
        fun bind(item: CalendarItem, onClick: (CalendarItem) -> Unit) {
            view.bind(item)
            view.setOnButtonClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = CalendarItemView(parent.context)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position], onItemClick)
    }

    override fun getItemCount(): Int = items.size
}