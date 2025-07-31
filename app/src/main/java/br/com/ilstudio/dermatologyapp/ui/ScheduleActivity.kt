package br.com.ilstudio.dermatologyapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import br.com.ilstudio.dermatologyapp.adapter.HourListAdapter
import br.com.ilstudio.dermatologyapp.databinding.ActivityScheduleBinding
import br.com.ilstudio.dermatologyapp.domain.model.AmPm
import br.com.ilstudio.dermatologyapp.domain.model.ItemHour

class ScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleBinding
    private var isYourself = true

    private val listHours = listOf(
        ItemHour("09:00", AmPm.AM, true), ItemHour("09:30", AmPm.AM, true), ItemHour("10:00", AmPm.AM, true),
        ItemHour("10:30", AmPm.AM, true), ItemHour("11:00", AmPm.AM, true), ItemHour("11:30", AmPm.AM, true),
        ItemHour("13:00", AmPm.PM, true), ItemHour("13:30", AmPm.PM, true), ItemHour("14:00", AmPm.PM, true),
        ItemHour("14:30", AmPm.PM, true), ItemHour("15:00", AmPm.PM, true), ItemHour("15:30", AmPm.PM, true),
        ItemHour("16:00", AmPm.PM, true), ItemHour("16:30", AmPm.PM, true), ItemHour("17:00", AmPm.PM, true)
    )

    private val adapter by lazy {
        HourListAdapter(listHours) { selectedHour ->
            println("Selected hour: $selectedHour")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupButtons()

        binding.buttonBack.setOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        binding.recyclerHours.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerHours.adapter = adapter
    }

    private fun setupButtons() {
        binding.buttonYourself.setOnButtonClickListener {
            isYourself = true
            binding.anotherForm.isVisible = false
            binding.buttonYourself.setTypeTag(true)
            binding.buttonAnother.setTypeTag(false)
        }

        binding.buttonAnother.setOnButtonClickListener {
            isYourself = false
            binding.anotherForm.isVisible = true
            binding.buttonYourself.setTypeTag(false)
            binding.buttonAnother.setTypeTag(true)
        }
    }
}
