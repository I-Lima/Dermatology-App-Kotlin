package br.com.ilstudio.dermatologyapp.data.repository

import android.app.Activity
import br.com.ilstudio.dermatologyapp.domain.model.RegistrationUser
import br.com.ilstudio.dermatologyapp.domain.model.User

class UserRepository(context: Activity) {
    private val firebaseAuthRepository = FirebaseAuthRepository(context)
    private val firestoreRepository = FirestoreRepository()

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
}