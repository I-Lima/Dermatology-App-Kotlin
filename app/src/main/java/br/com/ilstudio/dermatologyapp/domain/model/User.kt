package br.com.ilstudio.dermatologyapp.domain.model

import br.com.ilstudio.dermatologyapp.data.model.user.UserData
import br.com.ilstudio.dermatologyapp.utils.DateUtils.dateToTimestamp
import br.com.ilstudio.dermatologyapp.utils.DateUtils.localDate

class User(
    val id: String,
    val name: String,
    val email: String,
    val mobileNumber: String,
    val dateBirth: String? = null,
    val profilePicture: String? = null,
    val gender: String? = "Male"
) {
    fun toUserDataCreate(): UserData {
        return UserData(
            uid = this.id,
            name = this.name,
            email = this.email,
            mobileNumber = this.mobileNumber,
            dateBirth = if (!this.dateBirth.isNullOrEmpty()) dateToTimestamp(this.dateBirth) else null,
            profilePicture = this.profilePicture,
            gender = gender ?: "Male",
            createdAt = localDate(),
            updatedAt = localDate()
        )
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to this.id,
            "name" to this.name,
            "email" to this.email,
            "mobileNumber" to this.mobileNumber,
            "dateBirth" to if (!this.dateBirth.isNullOrEmpty()) dateToTimestamp(this.dateBirth) else null,
            "profilePicture" to this.profilePicture,
            "gender" to gender
        )
    }
}
