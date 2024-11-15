package br.com.ilstudio.dermatologyapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.ilstudio.dermatologyapp.data.repository.UserRepository
import br.com.ilstudio.dermatologyapp.databinding.ActivitySettingsBinding
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRepository = UserRepository(this)

        binding.header.setOnBackButtonClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }

        binding.itemNotification.setOnClickListener {}
        binding.itemAccount.setOnClickListener {
            lifecycleScope.launch {
                val result = userRepository.deleteAccount()

                result.fold({
                    Toast.makeText(baseContext, "Account deleted", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(baseContext, SplashScreenActivity::class.java))
                    finish()
                },{
                    Toast.makeText(baseContext, it.message, Toast.LENGTH_SHORT).show()
                })
            }

        }

    }
}
