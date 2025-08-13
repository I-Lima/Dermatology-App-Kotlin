package br.com.ilstudio.dermatologyapp.data.model.appointments

data class AppointmentsResponse(
    val success: Boolean,
    val data: List<AppointmentsData>? = null,
    val message: String? = null,
    val isEmpty: Boolean = false
)
