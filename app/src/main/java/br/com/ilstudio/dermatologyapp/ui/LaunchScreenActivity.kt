package br.com.ilstudio.dermatologyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.ilstudio.dermatologyapp.databinding.ActivityLaunchScreenBinding

class LaunchScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLaunchScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaunchScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogIn.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }

        binding.buttonSignIn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}