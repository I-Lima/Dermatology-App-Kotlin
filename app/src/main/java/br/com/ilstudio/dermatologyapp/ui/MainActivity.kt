package br.com.ilstudio.dermatologyapp.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import br.com.ilstudio.dermatologyapp.data.repository.FirestoreRepository
import br.com.ilstudio.dermatologyapp.databinding.ActivityMainBinding
import br.com.ilstudio.dermatologyapp.utils.DateUtils.timestampToDate
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE)

        lifecycleScope.launch {
            fetchUserData()
        }
    }

    private suspend fun fetchUserData() {
        val userId = sharedPreferences.getString("userId", null)
        val firestoreRepository = FirestoreRepository()

        if (userId != null) {
            val userData = firestoreRepository.getUser(userId)
            if (userData?.data == null) {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                return
            }

            val editor = sharedPreferences.edit()
            editor.putString("name", userData.data.name)
            editor.putString("email", userData.data.email)
            editor.putString("mobileNumber", userData.data.mobileNumber)
            editor.putString("dateBirth", timestampToDate(userData.data.dateBirth))
            editor.putString("profilePicture", userData.data.profilePicture)
            editor.apply()
        }
    }
}
