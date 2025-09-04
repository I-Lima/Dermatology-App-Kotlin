package br.com.ilstudio.dermatologyapp.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ilstudio.dermatologyapp.domain.model.ItemHour
import br.com.ilstudio.dermatologyapp.ui.customview.HourItemView

class ItemHourAdapter(
    private val items: List<ItemHour>,
    private val onSelecionado: (ItemHour) -> Unit
) : RecyclerView.Adapter<ItemHourAdapter.VH>() {

    private var selectedPos = RecyclerView.NO_POSITION

    inner class VH(val view: HourItemView) : RecyclerView.ViewHolder(view) {
        fun bind(item: ItemHour, isSelected: Boolean) {
            view.bind(item, isSelected)

            view.setOnButtonClickListener {
                if (!item.available) return@setOnButtonClickListener
                val prev = selectedPos
                selectedPos = bindingAdapterPosition
                if (prev != RecyclerView.NO_POSITION) notifyItemChanged(prev)
                notifyItemChanged(selectedPos)
                onSelecionado(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(HourItemView(parent.context))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position], position == selectedPos)
    }

    override fun getItemCount(): Int = items.size
}
