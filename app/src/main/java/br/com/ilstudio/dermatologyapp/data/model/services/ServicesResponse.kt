package br.com.ilstudio.dermatologyapp.data.model.services

data class ServicesResponse(
    val success: Boolean,
    val data: List<ServicesData>? = null,
    val message: String? = null,
    val isEmpty: Boolean = false
)
