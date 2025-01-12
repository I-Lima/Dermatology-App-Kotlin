package br.com.ilstudio.dermatologyapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.ilstudio.dermatologyapp.adapter.NotificationsAdapter
import br.com.ilstudio.dermatologyapp.data.repository.FirestoreRepositoryNotifications
import br.com.ilstudio.dermatologyapp.databinding.ActivityNotificationBinding
import kotlinx.coroutines.launch

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            val userId = intent.getStringExtra("userId") ?: ""
            if (userId.isNotEmpty()) {
                setList(userId)
            } else {
                Toast.makeText(
                    this@NotificationActivity,
                    "Error when get notifications",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.header.setOnBackButtonClickListener {
            finish()
        }
    }

    private suspend fun setList(userId: String){
        val firestoreRepositoryUsers = FirestoreRepositoryNotifications()
        val response = firestoreRepositoryUsers.getNotifications(userId)

        if (!response.success) {
            binding.textError.text = response.message
            binding.recyleview.visibility = View.GONE
            binding.error.visibility = View.VISIBLE
        }

        if (response.isEmpty) {
            binding.recyleview.visibility = View.GONE
            binding.noData.visibility = View.VISIBLE
        }

        binding.recyleview.layoutManager = LinearLayoutManager(this)
        binding.recyleview.adapter = NotificationsAdapter(response.data!!)
    }
}

