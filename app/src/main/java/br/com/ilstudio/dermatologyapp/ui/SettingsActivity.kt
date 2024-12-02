package br.com.ilstudio.dermatologyapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.data.repository.UserRepository
import br.com.ilstudio.dermatologyapp.databinding.ActivitySettingsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRepository = UserRepository(this)

        binding.header.setOnBackButtonClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }

        binding.itemNotification.setOnClickListener {
            startActivity(Intent(this, NotificationSettingActivity::class.java))
        }
        binding.itemAccount.setOnClickListener {
            val view: View = layoutInflater.inflate(R.layout.view_bottom_sheet_delete_account, null)
            val buttonCancel = view.findViewById<Button>(R.id.button_cancel)
            val buttonDelete = view.findViewById<Button>(R.id.button_delete)

            val dialog = BottomSheetDialog(this)
            dialog.setContentView(view)
            dialog.show()

            buttonCancel.setOnClickListener {
                dialog.dismiss()
            }

            buttonDelete.setOnClickListener {
                lifecycleScope.launch {
                    deleteAccount()
                }
                dialog.dismiss()
            }
        }
    }

    private suspend fun deleteAccount() {
        val result = userRepository.deleteAccount()

        result.fold({
            Toast.makeText(baseContext, "Account deleted", Toast.LENGTH_SHORT).show()

            startActivity(Intent(baseContext, LaunchScreenActivity::class.java))
            finish()
        },{
            Toast.makeText(baseContext, it.message, Toast.LENGTH_SHORT).show()
        })
    }
}
