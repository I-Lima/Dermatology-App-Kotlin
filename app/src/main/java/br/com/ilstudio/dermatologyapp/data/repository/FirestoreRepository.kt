package br.com.ilstudio.dermatologyapp.data.repository

import android.app.Activity
import android.content.Context
import br.com.ilstudio.dermatologyapp.data.model.user.NewAccount
import br.com.ilstudio.dermatologyapp.data.model.user.UserData
import br.com.ilstudio.dermatologyapp.data.model.user.UserResponse
import br.com.ilstudio.dermatologyapp.data.service.FirestoreService
import br.com.ilstudio.dermatologyapp.domain.model.User
import java.sql.Timestamp

class FirestoreRepository(private val context: Activity) {
    private val firestoreService = FirestoreService()
    private val sharedPreferences = context.getSharedPreferences("userData", Context.MODE_PRIVATE)

    /**
     * Saves a user object to the Firestore database.
     *
     * This function attempts to save the provided [User] object to Firestore. If the save operation
     * is successful, it returns a [UserResponse] indicating success. In case of an error, it catches
     * the exception and returns a [UserResponse] indicating failure, along with an error message.
     *
     * This is a **suspend** function, meaning it must be called within a coroutine or another suspend function.
     *
     * @param user The [User] object to be saved in the database.
     * @return A [UserResponse] indicating the success or failure of the save operation.
     *         If successful, the response will contain `true`; otherwise, it will include an error message.
     * @throws Exception If an error occurs while attempting to save the user.
     */

    suspend fun saveUser(user: User): UserResponse {
        return try {
            firestoreService.saveUser(user.id, user.toUserDataCreate())

            val editor = sharedPreferences.edit()
            editor.putString("userId", user.id)
            editor.apply()

            UserResponse(true)
        } catch (e: Exception) {
            UserResponse(false, null, "Register error")
        }
    }

    /**
     * Saves a user object to the Firestore database.
     *
     * This function attempts to save the provided [User] object to the Firestore database.
     * If the save operation is successful, it returns a [UserResponse] indicating success.
     * In the event of an error, it catches the exception and returns a [UserResponse] indicating failure,
     * along with a descriptive error message.
     *
     * This is a **suspend** function, meaning it must be called within a coroutine or another suspend function.
     *
     * @param user The [User] object to be saved in the database.
     * @return A [UserResponse] indicating the success or failure of the save operation.
     *         If successful, the response will contain `true`; otherwise, it will include an error message.
     * @throws Exception If an error occurs while attempting to save the user.
     */

    suspend fun getUser(uid: String): UserResponse {
         return try {
             val result = firestoreService.getUser(uid)
             val data = result?.data

             if(data.isNullOrEmpty()) {
                return UserResponse(true, null, "Data not found", true)
             }

             UserResponse(true, UserData(
                 data["uid"] as? String ?: "",
                 data["name"] as? String ?: "",
                 data["email"] as? String ?: "",
                 data["mobileNumber"] as? String ?: "",
                 data["dateBirth"] as? Timestamp ?: Timestamp(167234),
                 data["profilePicture"] as? String ?: "",
                 data["createdAt"] as? String ?: "",
                 data["updatedAt"] as? String ?: "",
             ))
         } catch (e: Exception) {
             UserResponse(false, null, "Get user error")
         }
    }

    /**
     * Updates an existing user object in the Firestore database.
     *
     * This function attempts to update the provided [UserData] object in the Firestore database.
     * If the update operation is successful, it returns a [UserResponse] indicating success.
     * If an error occurs during the update, it catches the exception and returns a [UserResponse]
     * indicating failure, along with a descriptive error message.
     *
     * This is a **suspend** function, meaning it must be called within a coroutine or another suspend function.
     *
     * @param user The [UserData] object containing the updated user information.
     * @return A [UserResponse] indicating the success or failure of the update operation.
     *         If successful, the response will contain `true`; otherwise, it will include an error message.
     * @throws Exception If an error occurs while attempting to update the user.
     */
    suspend fun updateUser(user: UserData): UserResponse {
        return try {
            firestoreService.updateUser(user)
            UserResponse(true)
        } catch (e: Exception) {
            UserResponse(false, null, "Error when update user. Please try later.")
        }
    }

    /**
     * Updates a Google account in the Firestore database.
     *
     * This function attempts to update the provided [NewAccount] object in the Firestore database.
     * If the update operation is successful, it returns a [UserResponse] indicating success.
     * If an error occurs during the update, it catches the exception and returns a [UserResponse]
     * indicating failure, along with a descriptive error message.
     *
     * This is a **suspend** function, meaning it must be called within a coroutine or another suspend function.
     *
     * @param user The [NewAccount] object containing the updated Google account information.
     * @return A [UserResponse] indicating the success or failure of the update operation.
     *         If successful, the response will contain `true`; otherwise, it will include an error message.
     * @throws Exception If an error occurs while attempting to update the Google account.
     */
    suspend fun updateGoogleAccount(user: NewAccount): UserResponse {
        return try {
            firestoreService.updateGoogleAccount(user)
            UserResponse(true)
        } catch (e: Exception) {
            UserResponse(false, null, "Error when update user. Please try later.")
        }
    }

    /**
     * Deletes a user from the Firestore database.
     *
     * This function attempts to delete the user associated with the provided user ID (UID)
     * from the Firestore database. If the deletion is successful, it returns a [UserResponse]
     * indicating success. If an error occurs during the deletion process, it catches the exception
     * and returns a [UserResponse] indicating failure, along with a descriptive error message.
     *
     * This is a **suspend** function, meaning it must be called within a coroutine or another suspend function.
     *
     * @param uid The user ID (UID) of the user to be deleted.
     * @return A [UserResponse] indicating the success or failure of the deletion operation.
     *         If successful, the response will contain `true`; otherwise, it will include an error message.
     * @throws Exception If an error occurs while attempting to delete the user.
     */
    suspend fun deleteUser(uid: String): UserResponse  {
        return try {
            firestoreService.deleteUser(uid)
            UserResponse(true)
        } catch (e: Exception) {
            UserResponse(false, null, "Error when try delete user. Please try later.")
        }
    }
}
