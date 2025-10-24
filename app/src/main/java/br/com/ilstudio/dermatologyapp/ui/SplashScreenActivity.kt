package br.com.ilstudio.dermatologyapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.ilstudio.dermatologyapp.data.service.FirebaseAuthService
import br.com.ilstudio.dermatologyapp.databinding.ActivitySplashScreenBinding

class SplashScreenActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var firestoreServiceAuth: FirebaseAuthService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        firestoreServiceAuth = FirebaseAuthService(this)
        setContentView(binding.root)

        val user = firestoreServiceAuth.getCurrentUser()
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("userId", user.uid)
            startActivity(intent)
        } else {
            startActivity(Intent(this, LogInActivity::class.java))
        }

        finish()
    }
}
