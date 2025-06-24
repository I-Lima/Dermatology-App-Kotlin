package br.com.ilstudio.dermatologyapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.ilstudio.dermatologyapp.databinding.ActivityScheduleBinding

class ScheduleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScheduleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}