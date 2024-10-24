package br.com.ilstudio.dermatologyapp.data.repository

import br.com.ilstudio.dermatologyapp.data.model.user.NewAccount
import br.com.ilstudio.dermatologyapp.data.model.user.UserData
import br.com.ilstudio.dermatologyapp.data.model.user.UserResponse
import br.com.ilstudio.dermatologyapp.data.service.FirestoreService
import br.com.ilstudio.dermatologyapp.domain.model.User
import java.sql.Timestamp

class FirestoreRepository {
    private val firestoreService = FirestoreService()

    suspend fun saveUser(user: User): UserResponse {
        return try {
            firestoreService.saveUser(user.id, user.toUserDataCreate())
            UserResponse(true)
        } catch (e: Exception) {
            UserResponse(false, null, "Register error")
        }
    }

    suspend fun getUser(uid: String): UserResponse {
         return try {
             val result = firestoreService.getUser(uid)
             val data = result?.data

             if(data.isNullOrEmpty()) {
                return UserResponse(true, null, "Data not found", true)
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

    suspend fun updateUser(user: UserData): UserResponse {
        return try {
            firestoreService.updateUser(user)
            UserResponse(true)
        } catch (e: Exception) {
            UserResponse(false, null, "Error when update user. Please try later.")
        }
    }

    suspend fun updateGoogleAccount(user: NewAccount): UserResponse {
        return try {
            firestoreService.updateGoogleAccount(user)
            UserResponse(true)
        } catch (e: Exception) {
            UserResponse(false, null, "Error when update user. Please try later.")
        }
    }

    suspend fun deleteUser(uid: String): UserResponse  {
        return try {
            firestoreService.deleteUser(uid)
            UserResponse(true)
        } catch (e: Exception) {
            UserResponse(false, null, "Error when try delete user. Please try later.")
        }
    }
}
