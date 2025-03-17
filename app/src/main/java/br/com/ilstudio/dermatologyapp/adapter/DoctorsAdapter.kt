package br.com.ilstudio.dermatologyapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.data.model.doctors.DoctorsData
import br.com.ilstudio.dermatologyapp.databinding.ItemDoctorBinding

class DoctorsAdapter(
    private var itemList: List<DoctorsData>,
    private val onFavItemClick: (DoctorsData) -> Unit
) : RecyclerView.Adapter<DoctorsAdapter.MyViewHolder>() {

    class MyViewHolder(
        private val binding: ItemDoctorBinding,
        private val onFavItemClick: (DoctorsData) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DoctorsData) {
            binding.img.setImageResource(setDoctors(item.photo))
            binding.name.text = item.name
            binding.speciality.text = item.expertise

            changeFavColor(item.favorite, binding)
            binding.fav.setOnClickListener {
                onFavItemClick(item)
                item.favorite = !item.favorite
                changeFavColor(item.favorite, binding)
            }
        }

        private fun changeFavColor(favorite: Boolean, binding: ItemDoctorBinding) {
            val color = if (favorite) R.color.white else R.color.primary
            val background = if (favorite) R.color.primary else R.color.white
            binding.fav.imageTintList = binding.root.context.getColorStateList(color)
            binding.fav.backgroundTintList = binding.root.context.getColorStateList(background)
        }

        companion object {
            fun setDoctors(type: Long): Int {
                return when (type) {
                    1L -> R.drawable.alex
                    2L -> R.drawable.icon_note_not
                    3L -> R.drawable.icon_chat_not
                    else -> R.drawable.icon_calendar_not
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onFavItemClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size
}
