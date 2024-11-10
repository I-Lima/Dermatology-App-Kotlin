package br.com.ilstudio.dermatologyapp.data.repository

import android.app.Activity
import android.content.Context
import br.com.ilstudio.dermatologyapp.data.service.FirebaseAuthService
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class FirebaseAuthRepository(context: Activity): FirebaseAuthService(context) {
    private val firebaseAuthService = FirebaseAuthService(context)
    private val sharedPreferences = context.getSharedPreferences("userData", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    /**
     * Asynchronously creates a new user account with the provided email and password.
     *
     * This function attempts to create a new user account using Firebase Authentication. If the account
     * creation is successful, it returns a [Result] with `true` indicating the user was created. If an error occurs,
     * the function returns a [Result.failure] with an appropriate error message.
     *
     * This is a **suspend** function, meaning it must be called within a coroutine or another suspend function.
     *
     * @param email The email address for the new user account.
     * @param password The password for the new user account.
     * @return [Result] containing `true` if the user is successfully created, or a [Result.failure] if an error occurs.
     *
     */
    suspend fun createUser(email: String, password: String): Result<Boolean> {
        return try {
            val authResult = firebaseAuthService.createUserWithEmailAndPassword(email, password)
            Result.success(authResult.user != null)
        } catch(e: FirebaseAuthException) {
            Result.failure(Exception("User already registered. Please try logging in."))
        }catch (e: Exception) {
            Result.failure(Exception("An error occurred in log in. Please try again later."))
        }
    }

    /**
     * Asynchronously signs in a user with the provided email and password.
     *
     * This function attempts to sign in a user using Firebase Authentication with the provided
     * email and password. If the sign-in is successful, it returns a [Result] with `true`. If an error occurs,
     * the function returns a [Result.failure] with an appropriate error message.
     *
     * This is a **suspend** function, meaning it must be called within a coroutine or another suspend function.
     *
     * @param email The email address of the user attempting to sign in.
     * @param password The password associated with the provided email.
     * @return [Result] containing `true` if the sign-in is successful, or a [Result.failure] if an error occurs.
     *
     */
    suspend fun signIn(email: String, password: String): Result<Boolean> {
        return try {
            val authResult = firebaseAuthService.signInWithEmailAndPassword(email, password)

            if (authResult.user != null) {
                editor.putString("userId", authResult.user!!.uid)
                editor.apply()
                Result.success(true)
            }
            Result.success(false)
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(Exception("User not found."))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(Exception("Invalid email or password"))
        } catch (e: Exception) {
            Result.failure(Exception("An error occurred in log in. Please try again later."))
        }
    }
}
