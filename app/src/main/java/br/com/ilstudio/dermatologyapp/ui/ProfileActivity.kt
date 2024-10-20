package br.com.ilstudio.dermatologyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.data.service.FirebaseAuthService
import br.com.ilstudio.dermatologyapp.databinding.ActivityProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var firebaseAuthService: FirebaseAuthService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuthService = FirebaseAuthService(this)

        binding.header.setOnBackButtonClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.buttonProfile.setOnButtonClickListener { }
        binding.buttonPrivacy.setOnButtonClickListener { }
        binding.buttonSettings.setOnButtonClickListener { }
        binding.buttonHelp.setOnButtonClickListener { }

        binding.buttonLogout.setOnButtonClickListener {
            val view: View = layoutInflater.inflate(R.layout.view_bottom_sheet, null)
            val buttonCancel = view.findViewById<Button>(R.id.button_cancel)
            val buttonLogout = view.findViewById<Button>(R.id.button_logout)

            val dialog = BottomSheetDialog(this)
            dialog.setContentView(view)
            dialog.show()

            buttonCancel.setOnClickListener {
                dialog.dismiss()
            }

            buttonLogout.setOnClickListener {
                logout()
                dialog.dismiss()
            }
        }
    }

    private fun logout() {
        firebaseAuthService.signOut()
        startActivity(Intent(this, LaunchScreenActivity::class.java))
        finish()
    }
}
