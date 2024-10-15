package br.com.ilstudio.dermatologyapp.data.remote

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private var auth: FirebaseAuth,
    private var context: Activity
) {
    /**
     * Creates a new user with the provided email address and password.
     *
     * This function uses Firebase Authentication to register a new user.
     * A completion listener is added to check if the operation was successful.
     *
     * @param email The email address of the user to be registered. It must be a valid email.
     * @param password The password of the user. It must be at least 6 characters long.
     * @return Returns `true` if the user registration is successful; otherwise, returns `false`.
     *
     * @throws FirebaseAuthException If an error occurs during authentication, such as an email already in use or a weak password.
     */
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Boolean {
        var value = false

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(context) { task ->
                println(task)
                if (task.isSuccessful) {
                    value = true
                }
            }.await()

        return value
    }

    /**
     * Signs in a user with the provided email address and password.
     *
     * This function uses Firebase Authentication to sign in an existing user.
     * A completion listener is added to check if the operation was successful.
     *
     * @param email The email address of the user attempting to sign in. It must be a valid email.
     * @param password The password of the user. It must match the password used during registration.
     * @return Returns `true` if the sign-in is successful; otherwise, returns `false`.
     *
     * @throws FirebaseAuthException If an error occurs during authentication, such as an incorrect password or a non-existing email.
     */
    suspend fun signInWithEmailAndPassword(email: String, password: String): Boolean {
        var value = false

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            println(task.isSuccessful)
            if (task.isSuccessful) {
                value = true
            }
        }.await()

        return value
    }

}