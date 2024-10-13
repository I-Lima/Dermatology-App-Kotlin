package br.com.ilstudio.dermatologyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.databinding.ActivityLogInBinding

class LogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    private lateinit var user: String
    private lateinit var pass: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener {
            startActivity(Intent(this, LaunchScreenActivity::class.java))
            finish()
        }

        binding.editUser.addTextChangedListener(loginTextWatcher)
        binding.editPass.addTextChangedListener(loginTextWatcher)

        binding.buttonHidden.setOnClickListener {
            val isHidden = binding.editPass.inputType == 129
            val icon = if (isHidden) R.drawable.icon_eye else R.drawable.icon_eye_close
            val type = if (isHidden) InputType.TYPE_CLASS_TEXT else 129

            binding.buttonHidden.setImageResource(icon)
            binding.editPass.inputType= type
        }

        binding.buttonLogIn2.setOnClickListener {
            if(!isValidEmail(user)) {
                binding.editUser.error = getString(R.string.invalid_email)
            }

            binding.textError.text = getString(R.string.invalid_email_mobile_number_or_password)
            binding.textError.text = ""

        }

        binding.buttonForget.setOnClickListener {

        }

        binding.buttonSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

    }

    private val loginTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            user = binding.editUser.text.toString()
            pass = binding.editPass.text.toString()

            binding.buttonLogIn2.isEnabled = user.isNotEmpty() && pass.isNotEmpty()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        return emailRegex.matches(email)
    }
}