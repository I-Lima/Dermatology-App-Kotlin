package br.com.ilstudio.dermatologyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import br.com.ilstudio.dermatologyapp.data.model.user.NewAccount
import br.com.ilstudio.dermatologyapp.data.repository.FirebaseAuthRepository
import br.com.ilstudio.dermatologyapp.data.repository.FirestoreRepository
import br.com.ilstudio.dermatologyapp.databinding.ActivityNewAccountGoogleBinding
import br.com.ilstudio.dermatologyapp.utils.Validators.isValidDate
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class NewAccountGoogleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewAccountGoogleBinding
    private lateinit var birth: String
    private lateinit var number: String
    private lateinit var firestoreRepository: FirestoreRepository
    private lateinit var firebaseAuthRepository: FirebaseAuthRepository
    private lateinit var user: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewAccountGoogleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestoreRepository = FirestoreRepository(this)
        firebaseAuthRepository = FirebaseAuthRepository(this)
        user = firebaseAuthRepository.getCurrentUser()!!

        binding.editNumber.addTextChangedListener(numberTextWatcher)
        binding.editBirth.addTextChangedListener(dateTextWatcher)

        binding.buttonSignIn2.setOnButtonClickListener {
            lifecycleScope.launch {
                validateFields()
            }
        }
    }

    private suspend fun validateFields() {
        val validDate = isValidDate(birth)
        if (validDate === "Minor") {
            binding.editBirth.error = "To register you must be 18 years old. Come back later."

            firestoreRepository.deleteUser(user.uid)
            user.delete()

            startActivity(Intent(this, LaunchScreenActivity::class.java))
            return finish()
        }
        if (validDate === "Invalid") {
            binding.editBirth.error = "Enter a valid date."
            return
        }
        if (validDate === "error") {
            binding.editBirth.error = "We have an error. Please check the date."
            return
        }

        val result = firestoreRepository.updateGoogleAccount(NewAccount(
            user.uid,
            number,
            birth
        ))

        if(!result.success) {
            binding.editBirth.error = result.errorMessage
        }

        Toast
            .makeText(baseContext, "User registered successfully", Toast.LENGTH_SHORT)
            .show()

        startActivity(Intent(baseContext, MainActivity::class.java))
        finish()
    }

    private val numberTextWatcher = object : TextWatcher {
        private var isUpdating: Boolean = false
        private val mask = "(##) #####-####"

        override fun afterTextChanged(s: Editable?) {
            number = binding.editNumber.text.toString()
            birth = binding.editBirth.text.toString()

            binding.buttonSignIn2.setActive(number.isNotEmpty() && birth.isNotEmpty())
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
            number = binding.editNumber.text.toString()
            birth = binding.editBirth.text.toString()

            binding.buttonSignIn2.setActive(number.isNotEmpty() && birth.isNotEmpty())
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
