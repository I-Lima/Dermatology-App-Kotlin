package br.com.ilstudio.dermatologyapp.data.repository

import android.app.Activity
import android.content.Context
import android.content.Intent
import br.com.ilstudio.dermatologyapp.domain.model.RegistrationUser
import br.com.ilstudio.dermatologyapp.domain.model.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class UserRepository(private val context: Activity) {
    private val firebaseAuthRepository = FirebaseAuthRepository(context)
    private val firestoreRepository = FirestoreRepository(context)
    private val sharedPreferences = context.getSharedPreferences("userData", Context.MODE_PRIVATE)

    /**
     * Registers a new user and adds their information to the database.
     *
     * This function attempts to create a new user account using the provided [RegistrationUser]
     * data. If the account creation is successful, it retrieves the current authenticated user,
     * constructs a [User] object with the user's details, and saves this information to the database
     * using Firestore. It returns a [Result] indicating the success or failure of the operation.
     *
     * This is a **suspend** function, meaning it must be called within a coroutine or another suspend function.
     *
     * @param user An instance of [RegistrationUser] containing the user's registration details.
     * @return A [Result] containing `true` if the user is successfully registered and added, or a [Result.failure]
     *         if an error occurs during the process.
     */
    suspend fun registerAndAddUser(user: RegistrationUser): Result<Boolean> {
        val result = firebaseAuthRepository.createUser(user.email, user.password)
        result.fold({
            val userAuth = firebaseAuthRepository.getCurrentUser()

            val registeredUser = User(
                userAuth?.uid ?: "",
                user.name,
                user.email,
                user.mobileNumber,
                user.dateBirth,
                null
            )

            val resultStore = firestoreRepository.saveUser(registeredUser)
            return Result.success(resultStore.success)
        }, {
            return Result.failure(it)
        })
    }

    /**
     * Handles the Google sign-in result and registers the user with Firebase.
     *
     * This function processes the result of a Google sign-in attempt by delegating the task
     * to the `firebaseAuthRepository`. If the sign-in is successful, it retrieves the currently
     * authenticated user and attempts to register the user in the system using [registerGoogleUser].
     * The function returns a [Result] indicating whether the entire process, including Google sign-in
     * and user registration, was successful.
     *
     * This is a **suspend** function, meaning it must be called within a coroutine or another suspend function.
     *
     * @param requestCode The request code received from the Google sign-in intent.
     * @param data The intent data containing the Google sign-in result.
     * @return A [Result] containing `true` if the Google sign-in and user registration are successful,
     *         or a [Result.failure] if an error occurs during the process.
     *
     */
    suspend fun handleGoogleSignInResult(requestCode: Int, data: Intent?): Result<Boolean> {
        val result = firebaseAuthRepository.handleGoogleSignInResult(requestCode, data)
        result.fold({
            val userAuth = firebaseAuthRepository.getCurrentUser()
            val resultRegister = userAuth?.let { it1 -> registerGoogleUser(it1) }

            resultRegister?.fold({
                return Result.success(it)
            }, {e ->
                return Result.failure(e)
            })
        }, {
            return Result.failure(it)
        })

        return Result.success(false)
    }

    /**
     * Registers a Google-authenticated user in the Firestore database.
     *
     * This function checks if a user authenticated via Google already exists in the Firestore database.
     * If the user is not found, it creates a new user record with the details from the [FirebaseUser] object
     * and saves it to Firestore. If the user registration is successful, it returns a [Result.success].
     * If the registration fails or the user already exists, the function deletes the authenticated
     * user and returns a [Result.failure].
     *
     * This is a **private suspend** function, meaning it must be called within a coroutine or another
     * suspend function, and it is accessible only within this class or file.
     *
     * @param userAuth The authenticated [FirebaseUser] object retrieved from Google sign-in.
     * @return A [Result] containing `true` if the user registration is successful, or a [Result.failure]
     *         if an error occurs or the user is already registered.
     *
     */
    private suspend fun registerGoogleUser(userAuth: FirebaseUser): Result<Boolean> {
        val user = firestoreRepository.getUser(userAuth.uid)
        val editor = sharedPreferences.edit()

        if (user.success && user.notFound == true) {
            val result = firestoreRepository.saveUser(User(
                userAuth.uid,
                userAuth.displayName ?: "",
                userAuth.email ?: "",
                "",
                null,
                userAuth.photoUrl.toString()
            ))

            if(result.success) {
                editor.putString("userId", userAuth.uid)
                editor.apply()
                return Result.success(true)
            }

            userAuth.delete().await()
            return Result.failure(Exception("Error when trying to register user. Please try later."))
        }

        editor.putString("userId", userAuth.uid)
        editor.apply()
        return Result.success(false)
    }

    /**
     * Deletes the currently authenticated user's account and removes their data from the Firestore database.
     *
     * This function attempts to delete the user's account using the `firebaseAuthRepository`. If the account
     * deletion is successful, it retrieves the user's unique identifier (`uid`) and removes the user's data
     * from the Firestore database using the `firestoreRepository`.
     *
     * If the process completes successfully, it returns a [Result] containing `true`. If an error occurs, it
     * returns a [Result.failure] with the exception details.
     *
     * This is a **private suspend** function, meaning it must be called within a coroutine or another suspend
     * function, and it is accessible only within this class or file.
     *
     * @return A [Result] containing `true` if the account and associated data are successfully deleted,
     *         or a [Result.failure] if an error occurs during the process.
     */
    suspend fun deleteAccount(): Result<Boolean> {
        val result = firebaseAuthRepository.deleteAccount()

        result.fold({
            val userId = firebaseAuthRepository.getCurrentUser()?.uid

            userId?.let {
                firestoreRepository.deleteUser(it)
            }

            return Result.success(true)
        }, {
            return Result.failure(Error("Error when update user. Please try later."))
        })
    }
}
