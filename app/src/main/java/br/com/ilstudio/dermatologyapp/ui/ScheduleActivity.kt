package br.com.ilstudio.dermatologyapp.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import br.com.ilstudio.dermatologyapp.adapter.HourListAdapter
import br.com.ilstudio.dermatologyapp.data.model.appointments.AppointmentsData
import br.com.ilstudio.dermatologyapp.data.repository.FirestoreRepositoryAppointments
import br.com.ilstudio.dermatologyapp.databinding.ActivityScheduleBinding
import br.com.ilstudio.dermatologyapp.domain.model.AmPm
import br.com.ilstudio.dermatologyapp.domain.model.ItemHour
import br.com.ilstudio.dermatologyapp.utils.AdapterLayout
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class ScheduleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScheduleBinding
    private lateinit var firestoreRepositoryAppointments: FirestoreRepositoryAppointments
    private lateinit var sharedPreferencesDoctor: SharedPreferences
    private lateinit var sharedPreferencesUser: SharedPreferences
    private var appointmentList: List<AppointmentsData>? = null
    private var isMale = false
    private var hour: ItemHour? = null
    private val user = mutableMapOf(
        "fullName" to "",
        "age" to "",
        "gender" to "",
        "description" to ""
    )

    private val listHours = listOf(
        ItemHour("09:00", AmPm.AM, true), ItemHour("09:30", AmPm.AM, true), ItemHour("10:00", AmPm.AM, true),
        ItemHour("10:30", AmPm.AM, true), ItemHour("11:00", AmPm.AM, true), ItemHour("11:30", AmPm.AM, true),
        ItemHour("13:00", AmPm.PM, true), ItemHour("13:30", AmPm.PM, true), ItemHour("14:00", AmPm.PM, true),
        ItemHour("14:30", AmPm.PM, true), ItemHour("15:00", AmPm.PM, true), ItemHour("15:30", AmPm.PM, true),
        ItemHour("16:00", AmPm.PM, true), ItemHour("16:30", AmPm.PM, true), ItemHour("17:00", AmPm.PM, true)
    )

    private val adapter by lazy {
        HourListAdapter(listHours) { selectedHour ->
            hour = selectedHour
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestoreRepositoryAppointments = FirestoreRepositoryAppointments()
        binding.recyclerHours.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerHours.adapter = adapter
        sharedPreferencesDoctor = getSharedPreferences("doctorData", Context.MODE_PRIVATE)
        val doctorId = sharedPreferencesDoctor.getString("doctor-id", "")
        val doctorName = sharedPreferencesDoctor.getString("doctor-name", "")
        sharedPreferencesUser = getSharedPreferences("userData", Context.MODE_PRIVATE)
        val userName = sharedPreferencesUser.getString("user-name", "")
//        val userAge = sharedPreferencesUser.getString("dateBirth", "")
        val spanCount = 3
        val spacing = (6 * resources.displayMetrics.density).toInt()
        val includeEdge = true

        binding.recyclerHours.addItemDecoration(
            AdapterLayout.GridSpacingItemDecoration(
                spanCount,
                spacing,
                includeEdge
            )
        )

        if (!doctorName.isNullOrBlank()) binding.title.text = doctorName

        lifecycleScope.launch {
            if (doctorId.isNullOrBlank()) return@launch

            val selectedDate = LocalDate.now()
            updateTimeList(listHours, selectedDate, doctorId)
        }

        binding.calendar.setOnDayClickListener {
            lifecycleScope.launch {
                if (doctorId.isNullOrBlank()) return@launch
                updateTimeList(listHours, it.searchDate, doctorId)
            }
        }

        binding.buttonYourself.setOnButtonClickListener {
            updatePatientDetailButtons(true)

            if (!userName.isNullOrBlank()) {
                binding.fullName.setTextInput(userName) //TODO: Não tá adicionando o texto no input.
                binding.fullName.isActivated = false
            }
        }

        binding.buttonAnother.setOnButtonClickListener {
            updatePatientDetailButtons(false)

            binding.fullName.setTextInput("")
            binding.fullName.isActivated = true
        }

        binding.buttonMale.setOnButtonClickListener {
            isMale = true
            binding.buttonMale.setTypeTag(true)
            binding.buttonFemale.setTypeTag(false)
        }

        binding.buttonFemale.setOnButtonClickListener {
            isMale = false
            binding.buttonMale.setTypeTag(false)
            binding.buttonFemale.setTypeTag(true)
        }

        binding.buttonBack.setOnClickListener { finish() }

        binding.buttonSaveAppoint.setOnButtonClickListener { }
    }

    private fun updatePatientDetailButtons(isYourself: Boolean) {
        binding.buttonYourself.setTypeTag(isYourself)
        binding.buttonAnother.setTypeTag(!isYourself)
    }

    private suspend fun getAppointments(doctorId: String, selectedDate: LocalDate): List<AppointmentsData>? {
        val response = firestoreRepositoryAppointments.getAppointmentsByDoctorId(doctorId, selectedDate)
        if (!response.success || response.data == null) return null

        return response.data
    }

    private suspend fun updateTimeList(
        listHours: List<ItemHour>,
        selectedDate: LocalDate,
        doctorId: String,
    ) {
        if (doctorId.isBlank()) return

        appointmentList = getAppointments(doctorId, selectedDate)
        if (appointmentList == null) return

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        println("selectedDate: $selectedDate")

        val appointmentsToday = appointmentList!!.filter { ap ->
            val apDate = ap.start_time
                .toDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            apDate == selectedDate
        }

        listHours.forEach { itemHour ->
            val itemTime = LocalTime.parse(itemHour.hour, formatter)

            val remove = appointmentsToday.any { ap ->
                val start = ap.start_time
                    .toDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalTime()
                    .truncatedTo(ChronoUnit.MINUTES)

                val end = ap.end_time
                    .toDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalTime()
                    .truncatedTo(ChronoUnit.MINUTES)

                val startRounded = start.withMinute((start.minute / 30) * 30).withSecond(0).withNano(0)
                val endRounded = end.withMinute((end.minute / 30) * 30).withSecond(0).withNano(0)

                itemTime >= startRounded && itemTime < endRounded
            }

            itemHour.available = !remove
        }

        adapter.notifyDataSetChanged()
    }
}
