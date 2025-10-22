package br.com.ilstudio.dermatologyapp.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.ilstudio.dermatologyapp.databinding.ActivityNotificationSettingBinding

class NotificationSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationSettingBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("SwitchPrefs", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        getAndSetSwitchState()

        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("switch_1", isChecked)
            editor.apply()
        }

        binding.switch2.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("switch_2", isChecked)
            editor.apply()
        }

        binding.switch3.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("switch_3", isChecked)
            editor.apply()
        }

        binding.switch4.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("switch_4", isChecked)
            editor.apply()
        }

        binding.switch5.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("switch_5", isChecked)
            editor.apply()
        }
    }

    private fun getAndSetSwitchState() {
        binding.switch1.isChecked = sharedPreferences.getBoolean("switch_1", false)
        binding.switch2.isChecked = sharedPreferences.getBoolean("switch_2", false)
        binding.switch3.isChecked = sharedPreferences.getBoolean("switch_3", false)
        binding.switch4.isChecked = sharedPreferences.getBoolean("switch_4", false)
        binding.switch5.isChecked = sharedPreferences.getBoolean("switch_5", false)
    }
}
