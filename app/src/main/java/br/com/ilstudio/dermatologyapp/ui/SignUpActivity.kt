package br.com.ilstudio.dermatologyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.Toast
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.data.remote.FirebaseAuthRepository
import br.com.ilstudio.dermatologyapp.databinding.ActivitySignUpBinding
import br.com.ilstudio.dermatologyapp.utils.Validators.isValidEmail
import br.com.ilstudio.dermatologyapp.utils.Validators.isValidPassword
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var name: String
    private lateinit var pass: String
    private lateinit var email: String
    private lateinit var number: String
    private lateinit var birth: String
    private lateinit var firebaseAuthRepository: FirebaseAuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuthRepository = FirebaseAuthRepository(this)
        firebaseAuthRepository.configureGoogleSignIn()

        binding.buttonBack.setOnClickListener {
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

        binding.buttonSignIn2.setOnClickListener {
            signIn()
        }

        binding.buttonGoogle.setOnClickListener {
            firebaseAuthRepository.signInWithGoogle()
        }

        binding.buttonLogIn.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        firebaseAuthRepository.handleGoogleSignInResult(requestCode, data, {
            Toast
                .makeText(this@SignUpActivity, "User registered successfully", Toast.LENGTH_SHORT)
                .show()

            startActivity(Intent(this, MainActivity::class.java))
        }, {

            binding.textError.text = "An error occurred while registering. Please try again later."
        })
    }

    private fun signIn() {
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
            var result = false
            CoroutineScope(Dispatchers.Main).launch {
                result = firebaseAuthRepository.createUserWithEmailAndPassword(email, pass)

                if (!result) {
                    binding.textError.text = "An error occurred while registering. Please try again later."
                }

                Toast
                    .makeText(this@SignUpActivity, "User registered successfully", Toast.LENGTH_SHORT)
                    .show()

                startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
            }
        }
    }

    private val loginTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            name = binding.editName.text.toString()
            pass = binding.editPass.text.toString()
            email = binding.editEmail.text.toString()
            number = binding.editNumber.text.toString()
            birth = binding.editBirth.text.toString()

            binding.buttonSignIn2.isEnabled = (name.isNotEmpty() && pass.isNotEmpty() &&
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

            binding.buttonSignIn2.isEnabled = (name.isNotEmpty() && pass.isNotEmpty() &&
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

            binding.buttonSignIn2.isEnabled = (name.isNotEmpty() && pass.isNotEmpty() &&
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

    /**
     * Validates the given date string and checks if it represents a valid date,
     * if the user is at least 18 years old, or if the date is invalid or a minor.
     *
     * This function:
     * - Parses the input date string in the format "dd/MM/yyyy".
     * - Compares the date to the current date.
     * - Returns "Valid" if the date corresponds to someone who is 18 years or older.
     * - Returns "Minor" if the age is less than 18 years.
     * - Returns "Invalid" if the date is in the future.
     * - Returns "error" if the date format is incorrect or parsing fails.
     *
     * @param dateStg The date string to validate, expected in the format "dd/MM/yyyy".
     * @return A string indicating the result: "Valid", "Minor", "Invalid", or "error".
     */
    private fun isValidDate(dateStg: String): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        try {
            val date = LocalDate.parse(dateStg, formatter)
            val today = LocalDate.now()

            if (date.isAfter(today)) return "Invalid"

            val age = Period.between(date, today).years
            if (age < 18) return "Minor"

            return "Valid"
        } catch (e: DateTimeParseException) {
            return "error"
        }
    }
}
