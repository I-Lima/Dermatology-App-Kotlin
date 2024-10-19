package br.com.ilstudio.dermatologyapp.domain.model

import br.com.ilstudio.dermatologyapp.data.model.user.UserData
import br.com.ilstudio.dermatologyapp.utils.DateUtils.dateToTimestamp
import br.com.ilstudio.dermatologyapp.utils.DateUtils.localDate

class User(
    val id: String,
    val name: String,
    val email: String,
    val mobileNumber: String,
    val dateBirth: String,
    val profilePicture: String?
) {
    fun toUserDataCreate(): UserData {
        return UserData(
            uid = this.id,
            name = this.name,
            email = this.email,
            mobileNumber = this.mobileNumber,
            dateBirth = dateToTimestamp(dateBirth),
            profilePicture = this.profilePicture,
            createdAt = localDate(),
            updatedAt = localDate()
        )
    }
}
