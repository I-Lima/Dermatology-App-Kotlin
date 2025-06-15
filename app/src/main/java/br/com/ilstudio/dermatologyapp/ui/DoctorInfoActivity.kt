package br.com.ilstudio.dermatologyapp.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.ilstudio.dermatologyapp.data.model.doctors.DoctorsData
import br.com.ilstudio.dermatologyapp.data.model.doctors.DoctorsDetailsResponse
import br.com.ilstudio.dermatologyapp.data.model.doctors.DoctorsDetailsData
import br.com.ilstudio.dermatologyapp.data.repository.FirestoreRepositoryDoctors
import br.com.ilstudio.dermatologyapp.databinding.ActivityDoctorInfoBinding
import kotlinx.coroutines.launch

class DoctorInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorInfoBinding
    private lateinit var firestoreRepositoryDoctors: FirestoreRepositoryDoctors
    private lateinit var data: DoctorsDetailsResponse
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var doctor: DoctorsData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorInfoBinding.inflate(layoutInflater)
        firestoreRepositoryDoctors = FirestoreRepositoryDoctors()
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("doctorData", Context.MODE_PRIVATE)

        lifecycleScope.launch {
            getData(sharedPreferences)
            data = firestoreRepositoryDoctors.getDoctorDetails(doctor.id)

            if (!data.success) {
                binding.data.visibility = View.GONE
                binding.textError.text = data.message
                binding.error.visibility = View.VISIBLE
                return@launch
            }

            data.data?.let { setData(it) }
        }
    }

    private fun getData(sharedPreferences: SharedPreferences) {
        val doctorId = sharedPreferences.getString("doctor-id", "")
        val doctorName = sharedPreferences.getString("doctor-name", "")
        val doctorExpertise = sharedPreferences.getString("doctor-expertise", "")
        val doctorPhoto = sharedPreferences.getLong("doctor-photo", 0)
        val doctorType = sharedPreferences.getString("doctor-type", "")
        val doctorFavorite = sharedPreferences.getBoolean("doctor-favorite", false)

        doctor = DoctorsData(doctorId!!, doctorName!!, doctorExpertise!!, doctorPhoto, doctorType!!, doctorFavorite)
    }

    private fun setData(data: DoctorsDetailsData) {
        binding.experienceText.text = "${data.experience} years experience".trim()
        binding.focusText.text = data.focus.trim()
        binding.img.setImageResource(doctor.photo)
        binding.name.text = doctor.name
        binding.expertise.text = doctor.expertise
        binding.comments.text = data.comments.toString()
        binding.stars.text = data.starts.toString()
        binding.schedule.text = data.date
        binding.profile.text = data.profile.trim()
        binding.carrerPath.text = data.careerPath.trim()
        binding.highlidghts.text = data.highlights.trim()
    }
}
