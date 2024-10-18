package br.com.ilstudio.dermatologyapp.data.model.user

data class UserResponse(
    val success: Boolean,
    val data: UserModel? = null,
    val errorMessage: String? = null
)
