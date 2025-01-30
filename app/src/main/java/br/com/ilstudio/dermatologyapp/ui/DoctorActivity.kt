package br.com.ilstudio.dermatologyapp.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.ilstudio.dermatologyapp.data.repository.FirestoreRepositoryDoctors
import br.com.ilstudio.dermatologyapp.databinding.ActivityDoctorBinding
import kotlinx.coroutines.launch

class DoctorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorBinding
    private lateinit var firestoreRepositoryDoctors: FirestoreRepositoryDoctors

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestoreRepositoryDoctors = FirestoreRepositoryDoctors()

        lifecycleScope.launch {
            getAllDoctorsData()
        }

        binding.header.setOnBackButtonClickListener {
            finish()
        }
    }

    private suspend fun getAllDoctorsData() {
        val response = firestoreRepositoryDoctors.getAll()

        if (!response.success) {
            binding.textError.text = response.message
            binding.recycle.visibility = View.GONE
            binding.error.visibility = View.VISIBLE
        }

        if (response.isEmpty) {
            binding.recycle.visibility = View.GONE
            binding.noData.visibility = View.VISIBLE
        }

        //binding.recycle.layoutManager = LinearLayoutManager(this)
    }
}