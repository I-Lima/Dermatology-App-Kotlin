package br.com.ilstudio.dermatologyapp.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.data.repository.FirestoreRepository
import br.com.ilstudio.dermatologyapp.databinding.ActivityEditProfileBinding
import br.com.ilstudio.dermatologyapp.domain.model.User
import br.com.ilstudio.dermatologyapp.ui.customview.CameraActivity
import br.com.ilstudio.dermatologyapp.utils.Convert.base64ToBitmap
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firestoreRepository: FirestoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE)
        firestoreRepository = FirestoreRepository()

        val userData = fetchUserData()
        if (userData == null) {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            return
        }

        setUserData(userData)

        binding.header.setOnBackButtonClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.imgUser.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }

        binding.buttonUpdate.setOnButtonClickListener {
            lifecycleScope.launch {
                updateUserData(userData.id)
            }
        }
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
        var photo = user.profilePicture

        if (!isValidUri(user.profilePicture)) {
            val base64Image = user.profilePicture?.let { base64ToBitmap(it) }
            photo = bitmapToPicassoUri(base64Image).toString()
        }

        if (!user.profilePicture.isNullOrEmpty()) {
            Picasso.get()
                .load(photo)
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

    private suspend fun updateUserData(userId: String) {
        val name = binding.editName.text.toString()
        val email = binding.editEmail.text.toString()
        val mobileNumber = binding.editNumber.text.toString()
        val dateBirth = binding.editBirth.text.toString()
        val profilePicture = binding.imgUser.toString()

        firestoreRepository.updateUser(User(
            userId,
            name,
            email,
            mobileNumber,
            dateBirth,
            profilePicture
        ).toUserDataCreate())
    }

    private fun isValidUri(uriString: String?): Boolean {
        if (uriString.isNullOrEmpty()) return false

        return try {
            val uri = Uri.parse(uriString)
            val validSchemes = listOf("http", "https", "content", "file")
            uri.scheme in validSchemes
        } catch (e: Exception) {
            false
        }
    }

    private fun bitmapToPicassoUri(bitmap: Bitmap?): Uri {
        val file = File(this.cacheDir, "temp_image.jpg")
        if (bitmap != null) {
            FileOutputStream(file).use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
        }
        return file.toUri()
    }
}