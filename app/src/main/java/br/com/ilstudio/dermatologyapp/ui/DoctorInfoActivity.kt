package br.com.ilstudio.dermatologyapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.ilstudio.dermatologyapp.data.model.doctors.DoctorsDetailsResponse
import br.com.ilstudio.dermatologyapp.data.repository.FirestoreRepositoryDoctors
import br.com.ilstudio.dermatologyapp.databinding.ActivityDoctorInfoBinding
import kotlinx.coroutines.launch

class DoctorInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorInfoBinding
    private lateinit var firestoreRepositoryDoctors: FirestoreRepositoryDoctors
    private lateinit var data: DoctorsDetailsResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorInfoBinding.inflate(layoutInflater)
        firestoreRepositoryDoctors = FirestoreRepositoryDoctors()
        setContentView(binding.root)

        lifecycleScope.launch {
            data = firestoreRepositoryDoctors.getDoctorDetails("")

            if (!data.success) {
            }
        }

    }
}