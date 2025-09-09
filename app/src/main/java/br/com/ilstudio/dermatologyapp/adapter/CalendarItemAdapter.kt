package br.com.ilstudio.dermatologyapp.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ilstudio.dermatologyapp.domain.model.CalendarItem
import br.com.ilstudio.dermatologyapp.ui.customview.CalendarItemView

class CalendarItemAdapter(
    private var items: List<CalendarItem>,
    private val onItemClick: (CalendarItem) -> Unit
) : RecyclerView.Adapter<CalendarItemAdapter.MyViewHolder>() {
    inner class MyViewHolder(val view: CalendarItemView) : RecyclerView.ViewHolder(view) {
        fun bind(item: CalendarItem) {
            view.bind(item)

            view.setOnButtonClickListener {
                if (item.type == 1) return@setOnButtonClickListener
                items.forEach { it.isSelected = false }
                item.isSelected = true
                onItemClick(item)
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(CalendarItemView(parent.context))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<CalendarItem>) {
        this.items = newItems
        notifyDataSetChanged()
    }
}
