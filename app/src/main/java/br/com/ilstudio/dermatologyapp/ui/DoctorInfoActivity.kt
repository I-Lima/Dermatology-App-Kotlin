package br.com.ilstudio.dermatologyapp.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.ilstudio.dermatologyapp.R
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
        binding.header.setOnBackButtonClickListener { finish() }
        binding.fav.setOnClickListener {
            doctor.favorite = !doctor.favorite
            changeFavColor(doctor.favorite, binding)
            firestoreRepositoryDoctors.updateFavoriteDoctor(doctor.id, doctor.favorite)
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
        binding.name.text = doctor.name
        binding.expertise.text = doctor.expertise
        binding.comments.text = data.comments.toString()
        binding.stars.text = data.starts.toString()
        binding.scheduleDate.text = data.date
        binding.scheduleHour.text = data.hour
        binding.profile.text = data.profile.trim()
        binding.carrerPath.text = data.careerPath.trim()
        binding.highlidghts.text = data.highlights.trim()

        changeFavColor(doctor.favorite, binding)
    }

    private fun changeFavColor(favorite: Boolean, binding: ActivityDoctorInfoBinding) {
        val color = if (favorite) R.color.white else R.color.primary
        val background = if (favorite) R.color.primary else R.color.white

        binding.fav.imageTintList = binding.root.context.getColorStateList(color)
        binding.fav.backgroundTintList = binding.root.context.getColorStateList(background)
    }
}
