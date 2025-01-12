package br.com.ilstudio.dermatologyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.data.model.notification.NotificationsData
import br.com.ilstudio.dermatologyapp.databinding.ItemNotificationBinding
import java.util.concurrent.TimeUnit

private fun setIcon(type: String): Int {
    return when (type) {
        "scheduled" -> R.drawable.icon_calendar_not
        "notes" -> R.drawable.icon_note_not
        "chat" -> R.drawable.icon_chat_not
        else -> R.drawable.icon_calendar_not
    }
}

private fun formatTimestampDifference(timestamp: Long): String {
    val currentTime = System.currentTimeMillis()
    val adjustedTimestamp = if (timestamp < 10000000000L) timestamp * 1000 else timestamp
    val differenceMillis = currentTime - adjustedTimestamp

    val hours = TimeUnit.MILLISECONDS.toHours(differenceMillis)
    val days = TimeUnit.MILLISECONDS.toDays(differenceMillis)
    val weeks = days / 7
    val months = days / 30
    val years = days / 365

    return when {
        hours < 24 -> "${hours} H"
        days < 7 -> "${days} D"
        weeks < 4 -> "${weeks} W"
        months < 12 -> "${months} M"
        else -> "${years} Y"
    }
}

class NotificationsAdapter(private val itemList: List<NotificationsData>):
    RecyclerView.Adapter<NotificationsAdapter.MyViewHolder>() {
    class MyViewHolder(private val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NotificationsData) {
            binding.image.setImageResource(setIcon(item.type))
            binding.title.text = item.title
            binding.description.text = item.description
            binding.date.text = formatTimestampDifference(item.date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = itemList.size
}
