package br.com.ilstudio.dermatologyapp.data.model.doctors

data class DoctorsDetailsResponse (
    val success: Boolean,
    val data: List<DoctorsDetailsData>? = null,
    val message: String? = null,
    val isEmpty: Boolean = false
)
