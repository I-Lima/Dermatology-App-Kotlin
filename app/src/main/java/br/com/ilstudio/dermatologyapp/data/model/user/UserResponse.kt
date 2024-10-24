package br.com.ilstudio.dermatologyapp.data.model.user

data class UserResponse(
    val success: Boolean,
    val data: UserData? = null,
    val errorMessage: String? = null,
    val notFound: Boolean? = null
)
