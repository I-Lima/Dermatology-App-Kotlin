package br.com.ilstudio.dermatologyapp.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.data.model.user.UserData
import br.com.ilstudio.dermatologyapp.data.repository.FirestoreRepositoryUsers
import br.com.ilstudio.dermatologyapp.databinding.ActivityMainBinding
import br.com.ilstudio.dermatologyapp.domain.model.User
import br.com.ilstudio.dermatologyapp.preference.UserPreference
import br.com.ilstudio.dermatologyapp.storage.SessionManager
import br.com.ilstudio.dermatologyapp.utils.DateUtils.timestampToDate
import com.squareup.picasso.Picasso
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

        setClick(binding)
    }

    private fun setUserData(userName: String, profilePicture: String?) {
        binding.userName.text = userName

        if (!profilePicture.isNullOrEmpty()) {
            Picasso.get()
                .load(profilePicture)
                .error(R.drawable.icon_person)
                .into(binding.imgUser)
        } else {
            binding.imgUser.setImageResource(R.drawable.icon_person)
        }

        dataLoading(false)
    }

    private suspend fun fetchUserData() {
        dataLoading(true)
        val userId = sharedPreferences.getString("userId", null)
        val firestoreRepositoryUsers = FirestoreRepositoryUsers(this)

        if (userId != null) {
            val userData = firestoreRepositoryUsers.getUser(userId)?.data
            if (userData == null) {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                return
            }

            setUserData(userData.name, userData.profilePicture)
            saveDataSession(userData)

            // Remove after
            val editor = sharedPreferences.edit()
            editor.putString("name", userData.name)
            editor.putString("email", userData.email)
            editor.putString("mobileNumber", userData.mobileNumber)
            editor.putString("dateBirth", timestampToDate(userData.dateBirth))
            editor.putString("profilePicture", userData.profilePicture)
            editor.apply()
        }
    }

    private suspend fun saveDataSession(data: UserData){
        UserPreference.saveUser(
            context = this@MainActivity,
            id = data.uid,
            name = data.name ?: "",
            birthdate = data.dateBirth,
            email = data.email ?: "",
            phone = data.mobileNumber ?: "",
            profilePicture = data.profilePicture,
            gender = data.gender
        )
        SessionManager.currentUser = User(
            id = data.uid,
            name = data.name ?: "",
            email = data.email ?: "",
            mobileNumber = data.mobileNumber ?: "",
            dateBirth = data.dateBirth.toString(),
            profilePicture = data.profilePicture,
            gender = data.gender
        )
    }

    private fun dataLoading(value: Boolean) {

        if (value) {
            binding.shimmerFrame.visibility = View.VISIBLE
            binding.shimmerFrame.startShimmer()
            binding.userData.visibility = View.GONE
        } else {
            binding.shimmerFrame.stopShimmer()
            binding.shimmerFrame.visibility = View.GONE
            binding.userData.visibility = View.VISIBLE
        }
    }

    private fun setClick(binding: ActivityMainBinding) {
        binding.iconNotifi.setOnClickListener {
            val userId = intent.getStringExtra("userId")
            val intent = Intent(this, NotificationActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
        binding.iconConfig.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        binding.iconDoctor.setOnClickListener {
            startActivity(Intent(this, DoctorActivity::class.java))
        }
        binding.iconFav.setOnClickListener {}
    }
}
