package br.com.ilstudio.dermatologyapp.domain.model

data class RegistrationUser (
    val name: String,
    val email: String,
    val password: String,
    val mobileNumber: String,
    val dateBirth: String,
)
