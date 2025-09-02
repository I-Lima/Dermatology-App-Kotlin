package br.com.ilstudio.dermatologyapp.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import br.com.ilstudio.dermatologyapp.adapter.HourListAdapter
import br.com.ilstudio.dermatologyapp.data.model.appointments.AppointmentsData
import br.com.ilstudio.dermatologyapp.data.model.appointments.AppointmentsResponse
import br.com.ilstudio.dermatologyapp.data.repository.FirestoreRepositoryAppointments
import br.com.ilstudio.dermatologyapp.databinding.ActivityScheduleBinding
import br.com.ilstudio.dermatologyapp.domain.model.AmPm
import br.com.ilstudio.dermatologyapp.domain.model.Appointment
import br.com.ilstudio.dermatologyapp.domain.model.ItemHour
import br.com.ilstudio.dermatologyapp.utils.AdapterLayout
import br.com.ilstudio.dermatologyapp.utils.DateUtils
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class ScheduleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScheduleBinding
    private lateinit var firestoreRepositoryAppointments: FirestoreRepositoryAppointments
    private lateinit var appointmentList: AppointmentsResponse
    private lateinit var sharedPreferencesDoctor: SharedPreferences
    private lateinit var sharedPreferencesUser: SharedPreferences
    private var isMale = false
    private var selectedDate: LocalDate = LocalDate.now()
    private var hour: ItemHour? = null
    private val user = mutableMapOf(
        "fullName" to "",
        "age" to "",
        "gender" to "",
    )
    private var description = ""

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
        sharedPreferencesDoctor = getSharedPreferences("doctorData", Context.MODE_PRIVATE)
        val doctorId = sharedPreferencesDoctor.getString("doctor-id", "")
        val doctorName = sharedPreferencesDoctor.getString("doctor-name", "")
        sharedPreferencesUser = getSharedPreferences("userData", Context.MODE_PRIVATE)
        val userUid = sharedPreferencesUser.getString("userId", "")
        val serviceId = "teste"
        val spanCount = 3
        val spacing = (6 * resources.displayMetrics.density).toInt()
        val includeEdge = true

        binding.recyclerHours.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerHours.adapter = adapter
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

            appointmentList = firestoreRepositoryAppointments.getAppointmentsByDoctorId(doctorId, selectedDate)
            if (!appointmentList.success || appointmentList.data == null) return@launch

            updateTimeList(listHours, appointmentList.data!!, selectedDate)
        }

        binding.buttonYourself.setOnButtonClickListener {
            updatePatientDetailButtons(true)

//            if (!userName.isNullOrBlank()) {
//                binding.fullName.setTextInput(userName) //TODO: Não tá adicionando o texto no input.
//                binding.fullName.isActivated = false
//            }
        }

        binding.buttonAnother.setOnButtonClickListener {
            updatePatientDetailButtons(false)

            binding.fullName.setTextInput("")
            binding.fullName.isActivated = true
        }

        binding.fullName.addTextChangedListener(formTextWatcher)
        binding.age.addTextChangedListener(formTextWatcher)
        binding.description.addTextChangedListener(formTextWatcher)

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

        binding.buttonSaveAppoint.setOnButtonClickListener {
            lifecycleScope.launch {
                val startTime = DateUtils.toTimestamp(selectedDate.toString(), hour?.hour ?: "")
                val endTime = DateUtils.addMinutesIntoTimestamp(startTime, 30);

                if (startTime == null || endTime == null) return@launch

                val data = Appointment(
                    doctorUid = doctorId ?: "",
                    userUid =  userUid ?: "",
                    servicesUid = serviceId ?: "",
                    startTime = startTime,
                    endTime = endTime,
                    description = description,
                    status = 0
                )

                saveAppointmentData(data)
            }
        }
    }

    private fun isValid (data: MutableMap<String, String>): Boolean {
        return data.values.all { it != "" }
    }

    private val formTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val gender = if (isMale) "Male" else "Female"
            user.set("fullName", binding.fullName.text.toString())
            user.set("age", binding.age.text.toString())
            user.set("gender", gender)
            description =  binding.description.text.toString()

            binding.buttonSaveAppoint.setActive(isValid(user))
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private suspend fun saveAppointmentData(data: Appointment) {
        val response = firestoreRepositoryAppointments.createAppointment(data)
    }

    private fun updatePatientDetailButtons(isYourself: Boolean) {
        binding.buttonYourself.setTypeTag(isYourself)
        binding.buttonAnother.setTypeTag(!isYourself)
    }

    private fun updateTimeList(
        listHours: List<ItemHour>,
        appointments: List<AppointmentsData>,
        selectedDate: LocalDate
    ) {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        val appointmentsToday = appointments.filter { ap ->
            val apDate = ap.startTime
                .toDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            apDate == selectedDate
        }

        listHours.forEach { itemHour ->
            val itemTime = LocalTime.parse(itemHour.hour, formatter)

            val remove = appointmentsToday.any { ap ->
                val start = ap.startTime
                    .toDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalTime()
                    .truncatedTo(ChronoUnit.MINUTES)

                val end = ap.endTime
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
