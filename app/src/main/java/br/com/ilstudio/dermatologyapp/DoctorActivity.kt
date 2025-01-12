package br.com.ilstudio.dermatologyapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.ilstudio.dermatologyapp.databinding.ActivityDoctorBinding

class DoctorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}