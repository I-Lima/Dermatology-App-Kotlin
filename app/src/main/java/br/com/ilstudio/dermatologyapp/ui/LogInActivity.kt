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
import br.com.ilstudio.dermatologyapp.databinding.ActivityLogInBinding
import br.com.ilstudio.dermatologyapp.utils.Validators.isValidEmail
import kotlinx.coroutines.launch

class LogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    private lateinit var user: String
    private lateinit var pass: String
    private lateinit var firebaseAuthRepository: FirebaseAuthRepository
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuthRepository = FirebaseAuthRepository(this)
        userRepository = UserRepository(this)

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
                val result = firebaseAuthRepository.signIn(user, pass)
                result.fold({
                        startActivity(Intent(this@LogInActivity, MainActivity::class.java))
                    },
                    { exception ->
                        binding.textError.text = exception.message
                    })
            }
        }

        binding.buttonGoogle.setOnClickListener {
            firebaseAuthRepository.signInWithGoogle()
        }

        binding.buttonForget.setOnClickListener {

        }

        binding.buttonSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        lifecycleScope.launch {
            val result = userRepository.handleGoogleSignInResult(requestCode, data)
            result.fold({
                if(it) {
                    Toast
                        .makeText(this@LogInActivity, "User registered successfully", Toast.LENGTH_SHORT)
                        .show()
                }

                startActivity(Intent(this@LogInActivity, MainActivity::class.java))
            }, {
                binding.textError.text = it.message
            })
        }
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
