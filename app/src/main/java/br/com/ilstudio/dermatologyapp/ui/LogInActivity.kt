package br.com.ilstudio.dermatologyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import androidx.lifecycle.lifecycleScope
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.data.service.FirebaseAuthService
import br.com.ilstudio.dermatologyapp.databinding.ActivityLogInBinding
import br.com.ilstudio.dermatologyapp.utils.Validators.isValidEmail
import kotlinx.coroutines.launch

class LogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    private lateinit var user: String
    private lateinit var pass: String
    private lateinit var firebaseAuthService: FirebaseAuthService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuthService = FirebaseAuthService(this)
        firebaseAuthService.configureGoogleSignIn()

        binding.header.setOnBackButtonClickListener {
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

        binding.buttonLogIn2.setOnButtonClickListener {
            if(!isValidEmail(user)) {
                binding.editUser.error = getString(R.string.invalid_email)
                return@setOnButtonClickListener
            }

            lifecycleScope.launch {
                val result = firebaseAuthService.signInWithEmailAndPassword(user, pass)
                result.fold({
                        startActivity(Intent(this@LogInActivity, MainActivity::class.java))
                    },
                    { exception ->
                        binding.textError.text = exception.message
                    })
            }

            binding.textError.text = getString(R.string.invalid_email_mobile_number_or_password)
            binding.textError.text = ""

        }

        binding.buttonGoogle.setOnClickListener {
            firebaseAuthService.signInWithGoogle()
        }

        binding.buttonForget.setOnClickListener {

        }

        binding.buttonSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        firebaseAuthService.handleGoogleSignInResult(requestCode, data, {
            startActivity(Intent(this, MainActivity::class.java))
        }, {
            binding.textError.text = "An error occurred in log in. Please try again later."
        })
    }

    private val loginTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            user = binding.editUser.text.toString()
            pass = binding.editPass.text.toString()

            binding.buttonLogIn2.setActive(user.isNotEmpty() && pass.isNotEmpty())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}
