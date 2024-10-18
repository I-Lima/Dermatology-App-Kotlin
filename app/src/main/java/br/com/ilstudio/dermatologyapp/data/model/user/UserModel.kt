package br.com.ilstudio.dermatologyapp.data.model.user

import java.sql.Date
import java.sql.Timestamp
import java.util.Base64


class UserModel(
    val uid: String,
    val fullName: String,
    val email: String,
    val mobileNumber: String,
    val dateBirth: Timestamp,
    val userPicture: String?,
    val createdAt: String,
    val updatedAt: String
) {}

