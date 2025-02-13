package br.com.ilstudio.dermatologyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.data.model.doctors.DoctorsData
import br.com.ilstudio.dermatologyapp.databinding.ItemDoctorBinding

private fun setDoctors(type: Long): Int {
    return when (type) {
        1L -> R.drawable.alex
        2L -> R.drawable.icon_note_not
        3L -> R.drawable.icon_chat_not
        else -> R.drawable.icon_calendar_not
    }
}

class DoctorsAdapter(private val itemList: List<DoctorsData>):
    RecyclerView.Adapter<DoctorsAdapter.MyViewHolder>() {
    class MyViewHolder(private val binding: ItemDoctorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DoctorsData) {
            binding.img.setImageResource(setDoctors(item.photo))
            binding.name.text = item.name
            binding.speciality.text = item.expertise
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
        val binding = ItemDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = itemList.size
}