package br.com.ilstudio.dermatologyapp.domain.model

import java.time.LocalDate

data class CalendarItem(
    val date: String,
    val day: String,
    var type: Int,
    val searchDate: LocalDate,
    var isSelected: Boolean = false
)
