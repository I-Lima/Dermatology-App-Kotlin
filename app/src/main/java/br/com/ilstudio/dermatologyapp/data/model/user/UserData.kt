package br.com.ilstudio.dermatologyapp.data.model.user

import br.com.ilstudio.dermatologyapp.domain.model.User
import java.sql.Timestamp

data class UserData(
    val uid: String,
    val name: String,
    val email: String,
    val mobile_number: String,
    val date_birth: Timestamp,
    val profile_picture: String?,
    val created_at: String,
    val updated_at: String
)
