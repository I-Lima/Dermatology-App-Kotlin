package br.com.ilstudio.dermatologyapp.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.databinding.ActivityEditProfileBinding
import br.com.ilstudio.dermatologyapp.domain.model.User
import com.squareup.picasso.Picasso

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE)

        val userData = fetchUserData()
        if (userData == null) {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            return
        }

        setUserData(userData)
    }

    private fun fetchUserData(): User? {
        val userId = sharedPreferences.getString("userId", null)
        val name = sharedPreferences.getString("name", null)
        val email = sharedPreferences.getString("email", null)
        val mobileNumber = sharedPreferences.getString("mobileNumber", null)
        val datebirth = sharedPreferences.getString("dateBirth", null)
        val imgUrl = sharedPreferences.getString("profilePicture", null)

        if (userId ==null || name == null || email == null || mobileNumber == null || datebirth == null) {
            return null
        }

        return User(userId, name, email, mobileNumber, datebirth, imgUrl)
    }

    private fun setUserData(user: User) {
        if (!user.profilePicture.isNullOrEmpty()) {
            Picasso.get()
                .load(user.profilePicture)
                .error(R.drawable.icon_person)
                .into(binding.imgUser)
        } else {
            binding.imgUser.setImageResource(R.drawable.icon_person)
        }

        binding.editName.setText(user.name)
        binding.editEmail.setText(user.email)
        binding.editNumber.setText(user.mobileNumber)
        binding.editBirth.setText(user.dateBirth)
    }
}