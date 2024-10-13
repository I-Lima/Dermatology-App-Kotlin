package br.com.ilstudio.dermatologyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.databinding.ActivitySignUpBinding
import java.time.LocalDate
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var name: String
    private lateinit var pass: String
    private lateinit var email: String
    private lateinit var number: String
    private lateinit var birth: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener {
            startActivity(Intent(this, LaunchScreenActivity::class.java))
            finish()
        }

        binding.editName.addTextChangedListener(loginTextWatcher)
        binding.editPass.addTextChangedListener(loginTextWatcher)
        binding.editEmail.addTextChangedListener(loginTextWatcher)
        binding.editNumber.addTextChangedListener(loginTextWatcher)
        binding.editBirth.addTextChangedListener(loginTextWatcher)

        binding.buttonHidden.setOnClickListener {
            val isHidden = binding.editPass.inputType == 129
            val icon = if (isHidden) R.drawable.icon_eye else R.drawable.icon_eye_close
            val type = if (isHidden) InputType.TYPE_CLASS_TEXT else 129

            binding.buttonHidden.setImageResource(icon)
            binding.editPass.inputType= type
        }

        binding.buttonSignIn2.setOnClickListener {
            if (!isValidEmail(email)) {
                binding.editEmail.error = getString(R.string.invalid_email)
            }

            val validDate = isValidDate(birth)
            if(validDate === "minor") {
                binding.editEmail.error = "To register you must be 18 years old. Come back later."
            }
            if (validDate === "invalid") {
                binding.editEmail.error = "Enter a valid date."
            }
            if (validDate === "error") {
                binding.editEmail.error = "We has a error. Come back later."
            }



        }

        binding.buttonLogIn.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
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

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        return emailRegex.matches(email)
    }

    private fun isValidDate(date: String): String {
        try {
            val today = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val userDate: LocalDate = LocalDate.parse(date, formatter)
            val period: Duration = Duration.between(today, userDate)

            if (period < 18) return "minor"
            if (period <= 0) return "invalid"

            return "valid"
        } catch (e: DateTimeParseException) {
            return "error"
        }
    }
}
