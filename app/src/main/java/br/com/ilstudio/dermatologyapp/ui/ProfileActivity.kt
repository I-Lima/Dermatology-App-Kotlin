package br.com.ilstudio.dermatologyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.ilstudio.dermatologyapp.data.remote.FirebaseAuthRepository
import br.com.ilstudio.dermatologyapp.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var firebaseAuthRepository: FirebaseAuthRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.setOnBackButtonClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.buttonProfile.setOnClickListener { }
        binding.buttonPrivacy.setOnClickListener { }
        binding.buttonSettings.setOnClickListener { }
        binding.buttonHelp.setOnClickListener { }

        binding.buttonLogout.setOnClickListener {
            firebaseAuthRepository.signOut()
            startActivity(Intent(this, LaunchScreenActivity::class.java))
            finish()
        }
    }
}