package br.com.ilstudio.dermatologyapp.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.data.repository.FirestoreRepository
import br.com.ilstudio.dermatologyapp.databinding.ActivityEditProfileBinding
import br.com.ilstudio.dermatologyapp.domain.model.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firestoreRepository: FirestoreRepository
    private lateinit var userSave: Map<String, Any?>
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var number: String
    private lateinit var birth: String
    private var userData: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE)
        firestoreRepository = FirestoreRepository(this)


        userData = fetchUserData()
        if (userData == null) {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            return
        }

        userSave = userData!!.toMap()
        setUserData(userData!!)

        binding.editName.addTextChangedListener(loginTextWatcher)
        binding.editEmail.addTextChangedListener(loginTextWatcher)
        binding.editNumber.addTextChangedListener(numberTextWatcher)
        binding.editBirth.addTextChangedListener(dateTextWatcher)

        binding.header.setOnBackButtonClickListener {
            if (!isIgual(userSave, userData!!.toMap())) {
                showMaterialDialog()
                return@setOnBackButtonClickListener
            }

            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }

        binding.buttonUpdate.setOnButtonClickListener {
            lifecycleScope.launch {
                updateUserData(userSave["id"].toString())
            }
        }
    }

    private fun showMaterialDialog() {
        MaterialAlertDialogBuilder(this)
            .setMessage("Do you want to go ahead and undo the changes?")
            .setPositiveButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Continue") { dialog, _ ->
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
            .show()
    }

    private fun fetchUserData(): User? {
        val userId = sharedPreferences.getString("userId", null)
        val name = sharedPreferences.getString("name", null)
        val email = sharedPreferences.getString("email", null)
        val mobileNumber = sharedPreferences.getString("mobileNumber", null)
        val datebirth = sharedPreferences.getString("dateBirth", null)
        val imgUrl = sharedPreferences.getString("profilePicture", null)

        if (userId == null || name == null || email == null || mobileNumber == null || datebirth == null) {
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

    private suspend fun updateUserData(userId: String) {
        binding.buttonUpdate.showLoading(true)

        val name = binding.editName.text.toString()
        val email = binding.editEmail.text.toString()
        val mobileNumber = binding.editNumber.text.toString()
        val dateBirth = binding.editBirth.text.toString()

        val result = firestoreRepository.updateUser(User(
            userId,
            name,
            email,
            mobileNumber,
            dateBirth
        ).toUserDataCreate())

        if (!result.success) {
            binding.buttonUpdate.showLoading(false)
            Toast.makeText(this, result.errorMessage, Toast.LENGTH_LONG).show()
            return
        }

        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("email", email)
        editor.putString("mobileNumber", mobileNumber)
        editor.putString("dateBirth", dateBirth)
        editor.apply()

        binding.buttonUpdate.showLoading(false)

        startActivity(Intent(this, ProfileActivity::class.java))
        finish()
    }

    private fun isIgual(a: Any?, b: Any?): Boolean {return a == b}

    private val loginTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            name = binding.editName.text.toString()
            email = binding.editEmail.text.toString()
            number = binding.editNumber.text.toString()
            birth = binding.editBirth.text.toString()
            userData = User(name, email, number, birth)

            binding.buttonUpdate.setActive(!isIgual(userSave, userData!!.toMap() ))
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private val numberTextWatcher = object : TextWatcher {
        private var isUpdating: Boolean = false
        private val mask = "(##) #####-####"

        override fun afterTextChanged(s: Editable?) {
            name = binding.editName.text.toString()
            email = binding.editEmail.text.toString()
            number = binding.editNumber.text.toString()
            birth = binding.editBirth.text.toString()
            userData = User(name, email, number, birth)

            binding.buttonUpdate.setActive(!isIgual(userSave, userData!!.toMap() ))
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (isUpdating) {
                isUpdating = false
                return
            }

            var str = s.toString().replace(Regex("[^\\d]"), "")

            if (str.length > 11) {
                str = str.substring(0, 11)
            }

            var formatted = ""
            var i = 0
            for (m in mask.toCharArray()) {
                if (m != '#' && i < str.length) {
                    formatted += m
                } else {
                    if (i < str.length) {
                        formatted += str[i]
                        i++
                    }
                }
            }

            isUpdating = true
            binding.editNumber.setText(formatted)
            binding.editNumber.setSelection(formatted.length)
        }
    }

    private val dateTextWatcher = object : TextWatcher {
        private var isUpdating: Boolean = false
        private val mask = "##/##/####"

        override fun afterTextChanged(s: Editable?) {
            name = binding.editName.text.toString()
            email = binding.editEmail.text.toString()
            number = binding.editNumber.text.toString()
            birth = binding.editBirth.text.toString()
            userData = User(name, email, number, birth)

            binding.buttonUpdate.setActive(!isIgual(userSave, userData!!.toMap() ))
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (isUpdating) {
                isUpdating = false
                return
            }

            var str = s.toString().replace(Regex("[^\\d]"), "")

            if (str.length > 8) {
                str = str.substring(0, 8)
            }

            var formatted = ""
            var i = 0
            for (m in mask.toCharArray()) {
                if (m != '#') {
                    formatted += m
                } else {
                    if (i < str.length) {
                        formatted += str[i]
                        i++
                    }
                }
            }

            isUpdating = true
            binding.editBirth.setText(formatted)
            binding.editBirth.setSelection(formatted.length)
        }
    }
}