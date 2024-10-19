package br.com.ilstudio.dermatologyapp.data.model.user

import java.sql.Timestamp

data class UserData(
    val uid: String,
    val name: String,
    val email: String,
    val mobileNumber: String,
    val dateBirth: Timestamp,
    val profilePicture: String?,
    val createdAt: String,
    val updatedAt: String
)
