package br.com.ilstudio.dermatologyapp.services

import br.com.ilstudio.dermatologyapp.data.model.user.UserModel
import br.com.ilstudio.dermatologyapp.data.model.user.UserResponse
import br.com.ilstudio.dermatologyapp.data.remote.FirestoreRepository
import java.sql.Timestamp
import java.util.Base64

class FirestoreService {
    private val firestoreRepository = FirestoreRepository()

    suspend fun registerUser(user: UserModel): UserResponse {
        return try {
            firestoreRepository.saveUser(user.uid, user)

            return UserResponse(true)
        } catch (e: Exception) {
            return UserResponse(false, null, "Register error")
        }
    }

    suspend fun getUser(uid: String): UserResponse {
         return try {
             val result = firestoreRepository.getUser(uid)
             val data = result?.data ?: null

             if(data.isNullOrEmpty()) {
                return UserResponse(false, null, "Data not found")
             }

             return UserResponse(true, UserModel(
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
             return UserResponse(false, null, "Get user error")
         }
    }
}