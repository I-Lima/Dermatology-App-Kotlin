package br.com.ilstudio.dermatologyapp.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.data.repository.FirestoreRepositoryUsers
import br.com.ilstudio.dermatologyapp.databinding.ActivityMainBinding
import br.com.ilstudio.dermatologyapp.ui.shared.UserSharedViewModel
import br.com.ilstudio.dermatologyapp.utils.DateUtils.timestampToDate
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val userSharedViewModel: UserSharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE)

        lifecycleScope.launch {
            if (userSharedViewModel.userData.value?.uid != null) {
                userSharedViewModel.userData.value?.let {
                    return@launch setUserData(it.name, it.profilePicture)
                }
            }

            fetchUserData()
        }

        binding.iconNotifi.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            intent.putExtra("userId", userSharedViewModel.userData.value?.uid)
            startActivity(intent)
        }
        binding.iconConfig.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        binding.iconDoctor.setOnClickListener {}
        binding.iconFav.setOnClickListener {}
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
            val userData = firestoreRepositoryUsers.getUser(userId)
            if (userData.data == null) {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                return
            }

            setUserData(userData.data.name, userData.data.profilePicture)

            userSharedViewModel.userData.value = userData.data

            val editor = sharedPreferences.edit()
            editor.putString("name", userData.data.name)
            editor.putString("email", userData.data.email)
            editor.putString("mobileNumber", userData.data.mobileNumber)
            editor.putString("dateBirth", timestampToDate(userData.data.dateBirth))
            editor.putString("profilePicture", userData.data.profilePicture)
            editor.apply()
        }
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
}
