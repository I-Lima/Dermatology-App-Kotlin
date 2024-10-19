package br.com.ilstudio.dermatologyapp.data.repository

import br.com.ilstudio.dermatologyapp.data.model.user.UserData
import br.com.ilstudio.dermatologyapp.data.model.user.UserResponse
import br.com.ilstudio.dermatologyapp.data.service.FirestoreService
import java.sql.Timestamp

class FirestoreRepository {
    private val firestoreService = FirestoreService()

    suspend fun saveUser(user: UserData): UserResponse {
        return try {
            firestoreService.saveUser(user.uid, user)

            UserResponse(true)
        } catch (e: Exception) {
            UserResponse(false, null, "Register error")
        }
    }

    suspend fun getUser(uid: String): UserResponse {
         return try {
             val result = firestoreService.getUser(uid)
             val data = result?.data ?: null

             if(data.isNullOrEmpty()) {
                return UserResponse(false, null, "Data not found")
             }

             UserResponse(true, UserData(
                 data["uid"] as? String ?: "",
                 data["fullName"] as? String ?: "",
                 data["email"] as? String ?: "",
                 data["mobileNumber"] as? String ?: "",
                 data["dateBirth"] as? Timestamp ?: Timestamp(167234),
                 data["userPicture"] as? String ?: "",
                 data["createdAt"] as? String ?: "",
                 data["updatedAt"] as? String ?: "",
             ))
         } catch (e: Exception) {
             UserResponse(false, null, "Get user error")
         }
    }
}
