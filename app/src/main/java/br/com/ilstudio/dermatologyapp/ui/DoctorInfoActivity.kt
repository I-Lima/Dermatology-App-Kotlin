package br.com.ilstudio.dermatologyapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.databinding.ActivityDoctorInfoBinding

class DoctorInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.focusText.setText(HtmlCompat.fromHtml(getString(R.string.texto_teste), HtmlCompat.FROM_HTML_MODE_LEGACY))

    }
}