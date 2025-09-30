package br.com.ilstudio.dermatologyapp.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import br.com.ilstudio.dermatologyapp.adapter.ItemHourAdapter
import br.com.ilstudio.dermatologyapp.data.model.appointments.AppointmentsData
import br.com.ilstudio.dermatologyapp.data.model.services.ServicesData
import br.com.ilstudio.dermatologyapp.data.repository.FirestoreRepositoryAppointments
import br.com.ilstudio.dermatologyapp.data.repository.FirestoreRepositoryServices
import br.com.ilstudio.dermatologyapp.databinding.ActivityScheduleBinding
import br.com.ilstudio.dermatologyapp.domain.model.AmPm
import br.com.ilstudio.dermatologyapp.domain.model.Appointment
import br.com.ilstudio.dermatologyapp.domain.model.ItemHour
import br.com.ilstudio.dermatologyapp.storage.SessionManager
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
    private val firestoreRepositoryAppointments = FirestoreRepositoryAppointments()
    private val firestoreRepositoryServices = FirestoreRepositoryServices()
    private lateinit var sharedPreferencesDoctor: SharedPreferences
    private lateinit var sharedPreferencesUser: SharedPreferences
    private var services: List<ServicesData> = emptyList()
    private var doctorId: String? = null
    private var doctorName: String? = null
    private var userUid: String? = null
    private var appointmentList: List<AppointmentsData>? = null
    private var isMale = false
    private var selectedDate: LocalDate = LocalDate.now()
    private var hour: ItemHour? = null
    private var description = ""
    private var anotherType = false
    private var selectedService: ServicesData? = null

    private val user = mutableMapOf(
        "fullName" to "",
        "age" to "",
        "gender" to ""
    )

    private val listHours = listOf(
        ItemHour("09:00", AmPm.AM, true), ItemHour("09:30", AmPm.AM, true),
        ItemHour("10:00", AmPm.AM, true), ItemHour("10:30", AmPm.AM, true),
        ItemHour("11:00", AmPm.AM, true), ItemHour("11:30", AmPm.AM, true),
        ItemHour("13:00", AmPm.PM, true), ItemHour("13:30", AmPm.PM, true),
        ItemHour("14:00", AmPm.PM, true), ItemHour("14:30", AmPm.PM, true),
        ItemHour("15:00", AmPm.PM, true), ItemHour("15:30", AmPm.PM, true),
        ItemHour("16:00", AmPm.PM, true), ItemHour("16:30", AmPm.PM, true),
        ItemHour("17:00", AmPm.PM, true)
    )

    private val adapter by lazy {
        ItemHourAdapter(listHours) { selectedHour ->
            hour = selectedHour
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setYourself()
        initSharedPrefs()
        setupRecycler()
        setupListeners()

        lifecycleScope.launch {
            doctorId?.let {
                updateTimeList(listHours, selectedDate, it)
            }
            getServices()
        }
    }

    private fun initSharedPrefs() {
        sharedPreferencesDoctor = getSharedPreferences("doctorData", Context.MODE_PRIVATE)
        sharedPreferencesUser = getSharedPreferences("userData", Context.MODE_PRIVATE)
        doctorId = sharedPreferencesDoctor.getString("doctor-id", null)
        doctorName = sharedPreferencesDoctor.getString("doctor-name", null)
        userUid = sharedPreferencesUser.getString("userId", null)
        binding.title.text = doctorName ?: ""
    }

    private fun setupRecycler() {
        val spanCount = 3
        val spacing = (6 * resources.displayMetrics.density).toInt()
        binding.recyclerHours.apply {
            layoutManager = GridLayoutManager(this@ScheduleActivity, spanCount)
            adapter = this@ScheduleActivity.adapter
            addItemDecoration(AdapterLayout.GridSpacingItemDecoration(spanCount, spacing, true))
        }
    }

    private fun setupListeners() = with(binding) {
        calendar.setOnDayClickListener {
            lifecycleScope.launch {
                doctorId?.let { id -> updateTimeList(listHours, it.searchDate, id) }
            }
        }

        fullName.addTextChangedListener(formTextWatcher)
        age.addTextChangedListener(formTextWatcher)
        description.addTextChangedListener(formTextWatcher)

        buttonYourself.setOnButtonClickListener { setYourself() }
        buttonAnother.setOnButtonClickListener { setAnother() }

        buttonMale.setOnButtonClickListener { changeGender(true) }
        buttonFemale.setOnButtonClickListener { changeGender(false) }

        buttonBack.setOnClickListener { finish() }
        buttonSaveAppoint.setOnButtonClickListener { saveAppointment() }
    }

    private suspend fun getServices() {
        val servicesResponse = firestoreRepositoryServices.getAllServices()
        if (servicesResponse.success && !servicesResponse.data.isNullOrEmpty()) {
            services = servicesResponse.data!!
            setupSpinner()
        } else {
            Toast.makeText(this, "Erro ao carregar serviços", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            services.map { "${it.name} - R$${it.price}" }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.mySpinner.adapter = adapter

        binding.mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                selectedService = services[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun saveAppointment() = lifecycleScope.launch {
        val startTime = DateUtils.toTimestamp(selectedDate.toString(), hour?.hour ?: return@launch)
        val endTime = DateUtils.addMinutesIntoTimestamp(startTime, 30) ?: return@launch

        val serviceId = selectedService?.uid ?: return@launch
        val appointment = if (anotherType) {
            Appointment(
                doctorUid = doctorId.orEmpty(),
                userUid = userUid.orEmpty(),
                servicesUid = serviceId,
                startTime = startTime,
                endTime = endTime,
                description = description,
                status = 0,
            )
        } else {
            Appointment(
                doctorUid = doctorId.orEmpty(),
                userUid = "",
                servicesUid = serviceId,
                startTime = startTime,
                endTime = endTime,
                description = description,
                status = 0,
                fullName = user["fullName"].orEmpty(),
                gender = user["gender"].orEmpty(),
                age = user["age"].orEmpty(),
            )
        }

        println("DATA $appointment")
        firestoreRepositoryAppointments.createAppointment(appointment)
    }

    private suspend fun updateTimeList(
        listHours: List<ItemHour>,
        selectedDate: LocalDate,
        doctorId: String
    ) {
        appointmentList = getAppointments(doctorId, selectedDate)
        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        val appointmentsToday = appointmentList?.filter { ap ->
            val apDate = ap.startTime.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            apDate == selectedDate
        } ?: emptyList()

        listHours.forEach { itemHour ->
            val itemTime = LocalTime.parse(itemHour.hour, formatter)

            val taken = appointmentsToday.any { ap ->
                val start = ap.startTime.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()
                val end = ap.endTime.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()

                val startRounded = start.truncatedTo(ChronoUnit.MINUTES).withMinute((start.minute / 30) * 30)
                val endRounded = end.truncatedTo(ChronoUnit.MINUTES).withMinute((end.minute / 30) * 30)

                itemTime >= startRounded && itemTime < endRounded
            }
            itemHour.available = !taken
        }

        adapter.notifyDataSetChanged()
    }

    private suspend fun getAppointments(doctorId: String, selectedDate: LocalDate): List<AppointmentsData>? {
        val response = firestoreRepositoryAppointments.getAppointmentsByDoctorId(doctorId, selectedDate)
        return if (response.success) response.data else null
    }

    private val formTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            user["fullName"] = binding.fullName.text.toString()
            user["age"] = binding.age.text.toString()
            user["gender"] = if (isMale) "Male" else "Female"
            description = binding.description.text.toString()

            binding.buttonSaveAppoint.setActive(user.values.all { it.isNotBlank() })
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun setYourself() {
        updatePatientDetailButtons(true)

        val user = SessionManager.currentUser

        if  (user != null) {
            binding.fullName.setTextInput(user.name)
            changeGender(user.gender == "Male")
            binding.age.setTextInput(DateUtils.timestampToAge(user.dateBirth).toString())
        }
    }

    private fun setAnother() {
        updatePatientDetailButtons(false)
        binding.fullName.setTextInput("")
    }

    private fun changeGender(isMale: Boolean) {
        this.isMale = isMale
        binding.buttonMale.setTypeTag(isMale)
        binding.buttonFemale.setTypeTag(!isMale)
    }

    private fun updatePatientDetailButtons(isYourself: Boolean) {
        binding.buttonYourself.setTypeTag(isYourself)
        binding.buttonAnother.setTypeTag(!isYourself)
    }
}
