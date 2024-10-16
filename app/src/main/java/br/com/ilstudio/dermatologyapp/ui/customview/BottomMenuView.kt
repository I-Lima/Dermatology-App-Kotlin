package br.com.ilstudio.dermatologyapp.ui.customview

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.content.ContextCompat.getColor
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.ui.MainActivity
import br.com.ilstudio.dermatologyapp.ui.ProfileActivity

class BottomMenuView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {
    private var buttonHome: ImageView
    private var buttonProfile: ImageView
    private var buttonCalendar: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_bottom_menu, this, true)
        buttonHome = findViewById(R.id.button_home)
        buttonProfile = findViewById(R.id.button_profile)
        buttonCalendar = findViewById(R.id.button_calendar)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.BottomMenuView, 0, 0)

            val page = typedArray.getString(R.styleable.BottomMenuView_page) ?: "home"

            when(page) {
                "0" -> {
                    buttonHome.background = getDrawable(context, R.drawable.shape_view_bottom_menu_item)
                    buttonHome.imageTintList = ColorStateList.valueOf(getColor(context,R.color.primary))

                    buttonProfile.background = null
                    buttonProfile.imageTintList = ColorStateList.valueOf(getColor(context,R.color.white))
                    buttonCalendar.background = null
                    buttonCalendar.imageTintList = ColorStateList.valueOf(getColor(context,R.color.white))
                }
                "1" -> {
                    buttonProfile.background = getDrawable(context, R.drawable.shape_view_bottom_menu_item)
                    buttonProfile.imageTintList = ColorStateList.valueOf(getColor(context,R.color.primary))

                    buttonHome.background = null
                    buttonHome.imageTintList = ColorStateList.valueOf(getColor(context,R.color.white))
                    buttonCalendar.background = null
                    buttonCalendar.imageTintList = ColorStateList.valueOf(getColor(context,R.color.white))
                }
                "2" -> {
                    buttonCalendar.background = getDrawable(context, R.drawable.shape_view_bottom_menu_item)
                    buttonCalendar.imageTintList = ColorStateList.valueOf(getColor(context,R.color.primary))

                    buttonHome.background = null
                    buttonHome.imageTintList = ColorStateList.valueOf(getColor(context,R.color.white))
                    buttonProfile.background = null
                    buttonProfile.imageTintList = ColorStateList.valueOf(getColor(context,R.color.white))
                }
            }

            typedArray.recycle()
        }

        buttonHome.setOnClickListener {
            context.startActivity(Intent(context, MainActivity::class.java))
        }

        buttonProfile.setOnClickListener {
            context.startActivity(Intent(context, ProfileActivity::class.java))
        }

        buttonCalendar.setOnClickListener {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}
