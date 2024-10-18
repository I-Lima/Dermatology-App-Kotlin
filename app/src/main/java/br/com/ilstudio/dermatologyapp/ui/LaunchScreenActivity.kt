package br.com.ilstudio.dermatologyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import br.com.ilstudio.dermatologyapp.data.model.user.UserModel
import br.com.ilstudio.dermatologyapp.databinding.ActivityLaunchScreenBinding
import br.com.ilstudio.dermatologyapp.services.FirestoreService
import kotlinx.coroutines.launch
import java.sql.Timestamp

class LaunchScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLaunchScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaunchScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firestoreService = FirestoreService()

        binding.buttonLogIn.setOnButtonClickListener {
//            startActivity(Intent(this, LogInActivity::class.java))
            val user = UserModel(
                "123414141414141",
                "User teste",
                "test@test.com",
                "85876543214",
                Timestamp(16872364),
                null,
                "12-02-2002 23:23:2321",
                "12-02-2002 23:23:2321"
            )

            lifecycleScope.launch {
                val result = firestoreService.getUser(user.uid)
                println(result)
            }
        }

        binding.buttonSignIn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}
