package br.com.ilstudio.dermatologyapp.data.service

import android.app.Activity
import android.content.Intent
import br.com.ilstudio.dermatologyapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

open class FirebaseAuthService(private val context: Activity) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient

    init {
        configureGoogleSignIn()
    }

    /**
     * Configures Google Sign-In options for the application.
     *
     * This method sets up the GoogleSignInOptions object with default sign-in settings.
     * It requests the ID token using the web client ID from resources and requests
     * the user's email. The GoogleSignInClient is then initialized with these options.
     *
     * The web client ID is retrieved from the app's string resources, which should match
     * the client ID of the Google project configured in the Google Cloud Console.
     *
     */
    private fun configureGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    /**
     * Initiates the Google Sign-In process.
     *
     * This method launches an intent to start the Google Sign-In activity. It creates
     * the sign-in intent using the `googleSignInClient` and starts the activity with
     * the intent for a result. The result will be handled in the activity's `onActivityResult`
     * method, using the request code `RC_SIGN_IN`.
     *
     */
    fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        context.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    /**
     * Handles the result of a Google sign-in attempt.
     *
     * This function processes the result of a Google sign-in intent based on the provided request code.
     * If the sign-in is successful, it retrieves the Google account from the intent and attempts to
     * authenticate with Firebase using the account's ID token. The function returns a [Result] indicating
     * whether the sign-in and Firebase authentication were successful.
     *
     * This is an **internal suspend** function, meaning it must be called within a coroutine or another suspend function,
     * and it is accessible only within the module.
     *
     * @param requestCode The request code received from the Google sign-in intent.
     * @param data The intent data containing the Google sign-in result.
     * @return A [Result] containing `true` if the Google sign-in and Firebase authentication are successful,
     *         or a [Result.failure] if an error occurs during the process.
     *
     */
    internal suspend fun handleGoogleSignInResult(requestCode: Int, data: Intent?): Result<Boolean> {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    val result = firebaseAuthWithGoogle(it.idToken!!)
                    result.fold({
                        return Result.success(true)
                    },{ e ->
                        return Result.failure(e)
                    })
                }
            } catch (e: ApiException) {
                return Result.failure(e)
            }
        }

        return Result.success(false)
    }

    /**
     * Authenticates a user with Firebase using a Google ID token.
     *
     * This function signs in a user to Firebase using the Google ID token provided. It creates
     * a [GoogleAuthProvider] credential from the token and attempts to sign in with Firebase.
     * The function returns a [Result] indicating whether the authentication was successful or not.
     *
     * This is a **private suspend** function, meaning it must be called within a coroutine or another
     * suspend function, and it is accessible only within this class or file.
     *
     * @param idToken The Google ID token used for Firebase authentication.
     * @return A [Result] containing `true` if the Firebase authentication is successful, or a [Result.failure]
     *         if the authentication fails.
     *
     */
    private suspend fun firebaseAuthWithGoogle(idToken: String): Result<Boolean> {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(context) { task ->
                if (task.isSuccessful) {
                    Result.success(true)
                } else {
                    Result.failure(Exception("Authentication Failed."))
                }
            }.await()

        return Result.success(false)
    }

    /**
     * Asynchronously creates a new user with the provided email and password.
     *
     * This function uses Firebase Authentication to register a new user account with the given
     * email and password. The result of the account creation is returned as an [AuthResult] object.
     *
     * This is a **suspend** function, meaning it must be called within a coroutine or another
     * suspend function, and it will suspend execution until the user creation process is complete.
     *
     * @param email The email address for the new user account.
     * @param password The password to set for the new user account.
     * @return [AuthResult] containing details of the newly created user if successful.
     *
     */
    internal suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult {
        return auth.createUserWithEmailAndPassword(email, password).await()
    }

    /**
     * Asynchronously signs in a user with the provided email and password.
     *
     * This function uses Firebase Authentication to sign in a user with the given email
     * and password. The result of the sign-in process is returned as an [AuthResult] object.
     *
     * This is a **suspend** function, meaning it must be called within a coroutine or another
     * suspend function, and it will suspend execution until the sign-in process completes.
     *
     * @param email The email address of the user attempting to sign in.
     * @param password The password associated with the provided email.
     * @return [AuthResult] containing details of the authenticated user if the sign-in is successful.
     *
     */
    internal suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult {
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    /**
     * Signs the user out from both Firebase authentication and Google Sign-In.
     *
     * This method will:
     * - Sign out the current user from Firebase Authentication using the Firebase Auth instance.
     * - Sign out the current user from Google Sign-In using the `GoogleSignInClient`.
     *
     * It's important to call this method when the user explicitly wants to log out of the application.
     * After signing out, the user will need to authenticate again to access protected resources.
     */
    fun signOut() {
        auth.signOut()
        googleSignInClient.signOut()
    }

    /**
     * Retrieves the currently authenticated user, if any.
     *
     * This function returns the currently signed-in [FirebaseUser], or `null` if no user
     * is currently authenticated.
     *
     * @return The currently signed-in [FirebaseUser], or `null` if there is no authenticated user.
     */
    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
