package br.com.ilstudio.dermatologyapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.ilstudio.dermatologyapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.setOnBackButtonClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }

        binding.itemNotification.setOnClickListener {}
        binding.itemPassword.setOnClickListener {}
        binding.itemAccount.setOnClickListener {}

    }
}
