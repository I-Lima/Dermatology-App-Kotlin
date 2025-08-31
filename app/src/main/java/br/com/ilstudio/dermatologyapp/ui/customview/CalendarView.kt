package br.com.ilstudio.dermatologyapp.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.adapter.CalendarItemAdapter

class CalendarView @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {
    private var backButton: ImageView
    private var nextButton: ImageView
    private var recyclerView: RecyclerView


    init {
        LayoutInflater.from(context).inflate(R.layout.view_calendar, this, true)
        backButton = findViewById(R.id.back_button_component)
        nextButton = findViewById(R.id.next_button_component)
        recyclerView = findViewById(R.id.day_recycle)

        backButton.setOnClickListener {
            //TODO: Add logic to the previous week
        }
        nextButton.setOnClickListener {
            //TODO: Add logic to the next week
        }

        val items = null;
        val adapter = CalendarItemAdapter(items) { item ->
            Toast.makeText(this, "Clicou em ${item.day}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter
    }
}
