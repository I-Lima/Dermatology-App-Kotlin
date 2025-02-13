package br.com.ilstudio.dermatologyapp.data.model.doctors

data class DoctorsResponse (
    val success: Boolean,
    val data: List<DoctorsData>? = null,
    val message: String? = null,
    val isEmpty: Boolean = false
)
