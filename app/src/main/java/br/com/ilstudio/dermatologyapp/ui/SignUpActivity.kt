package br.com.ilstudio.dermatologyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.data.repository.FirebaseAuthRepository
import br.com.ilstudio.dermatologyapp.data.repository.UserRepository
import br.com.ilstudio.dermatologyapp.databinding.ActivitySignUpBinding
import br.com.ilstudio.dermatologyapp.domain.model.RegistrationUser
import br.com.ilstudio.dermatologyapp.utils.Validators.isValidDate
import br.com.ilstudio.dermatologyapp.utils.Validators.isValidEmail
import br.com.ilstudio.dermatologyapp.utils.Validators.isValidPassword
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var name: String
    private lateinit var pass: String
    private lateinit var email: String
    private lateinit var number: String
    private lateinit var birth: String
    private lateinit var firebaseAuthRepository: FirebaseAuthRepository
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuthRepository = FirebaseAuthRepository(this)
        userRepository = UserRepository(this)

        binding.header.setOnBackButtonClickListener {
            startActivity(Intent(this, LaunchScreenActivity::class.java))
            finish()
        }

        binding.editName.addTextChangedListener(loginTextWatcher)
        binding.editPass.addTextChangedListener(loginTextWatcher)
        binding.editEmail.addTextChangedListener(loginTextWatcher)
        binding.editNumber.addTextChangedListener(numberTextWatcher)
        binding.editBirth.addTextChangedListener(dateTextWatcher)

        binding.buttonHidden.setOnClickListener {
            val isHidden = binding.editPass.inputType == 129
            val icon = if (isHidden) R.drawable.icon_eye else R.drawable.icon_eye_close
            val type = if (isHidden) InputType.TYPE_CLASS_TEXT else 129

            binding.buttonHidden.setImageResource(icon)
            binding.editPass.inputType= type
        }

        binding.buttonSignIn2.setOnButtonClickListener {
            signUp()
        }

        binding.buttonGoogle.setOnClickListener {
            binding.buttonSignIn2.showLoading(true)
            firebaseAuthRepository.signInWithGoogle()
        }

        binding.buttonLogIn.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        binding.buttonSignIn2.showLoading(true)
        lifecycleScope.launch {
            val result = userRepository.handleGoogleSignInResult(requestCode, data)
            binding.buttonSignIn2.showLoading(false)
            result.fold({
                startActivity(Intent(this@SignUpActivity, NewAccountGoogleActivity::class.java))
            }, {
                binding.textError.text = it.message
            })
        }
    }

    private fun signUp() {
        binding.buttonSignIn2.showLoading(true)
        if (!isValidEmail(email)) {
            binding.editEmail.error = getString(R.string.invalid_email)
        }

        val validDate = isValidDate(birth)
        if (validDate === "Minor") {
            binding.editBirth.error = "To register you must be 18 years old. Come back later."
        }
        if (validDate === "Invalid") {
            binding.editBirth.error = "Enter a valid date."
        }
        if (validDate === "error") {
            binding.editBirth.error = "We have an error. Please check the date."
        }

        if(!isValidPassword(pass)){
            binding.editPass.error = "The password must have 8 digits, upper and lower case " +
                    "letters and special characters."
        }


        val emailError = binding.editEmail.error
        val dateError = binding.editBirth.error
        val passError = binding.editPass.error

        if(emailError.isNullOrEmpty() && dateError.isNullOrEmpty() && passError.isNullOrEmpty()) {
            binding.buttonSignIn2.showLoading(true)
            lifecycleScope.launch {
                val result = userRepository.registerAndAddUser(RegistrationUser(
                    name,
                    email,
                    pass,
                    number,
                    birth
                ))

                binding.buttonSignIn2.showLoading(false)
                result.fold({
                    Toast
                        .makeText(this@SignUpActivity, "User registered successfully", Toast.LENGTH_SHORT)
                        .show()

                    startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                }, {
                    binding.textError.text = it.message
                })
            }
        }

        binding.buttonSignIn2.showLoading(false)
    }

    private val loginTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            name = binding.editName.text.toString()
            pass = binding.editPass.text.toString()
            email = binding.editEmail.text.toString()
            number = binding.editNumber.text.toString()
            birth = binding.editBirth.text.toString()

            binding.buttonSignIn2.setActive(name.isNotEmpty() && pass.isNotEmpty() &&
                    email.isNotEmpty() && number.isNotEmpty() && birth.isNotEmpty())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private val numberTextWatcher = object : TextWatcher {
        private var isUpdating: Boolean = false
        private val mask = "(##) #####-####"

        override fun afterTextChanged(s: Editable?) {
            name = binding.editName.text.toString()
            pass = binding.editPass.text.toString()
            email = binding.editEmail.text.toString()
            number = binding.editNumber.text.toString()
            birth = binding.editBirth.text.toString()

            binding.buttonSignIn2.setActive(name.isNotEmpty() && pass.isNotEmpty() &&
                    email.isNotEmpty() && number.isNotEmpty() && birth.isNotEmpty())
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
            pass = binding.editPass.text.toString()
            email = binding.editEmail.text.toString()
            number = binding.editNumber.text.toString()
            birth = binding.editBirth.text.toString()

            binding.buttonSignIn2.setActive(name.isNotEmpty() && pass.isNotEmpty() &&
                    email.isNotEmpty() && number.isNotEmpty() && birth.isNotEmpty())
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
