package br.com.ilstudio.dermatologyapp.data.service


import android.app.Activity
import android.content.Intent
import android.widget.Toast
import br.com.ilstudio.dermatologyapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await


class FirebaseAuthService(private var context: Activity) {
    private val auth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient

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
    fun configureGoogleSignIn() {
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
     * Handles the result of the Google Sign-In process.
     *
     * This method is called after the Google Sign-In activity returns a result. It checks if the
     * `requestCode` matches the `RC_SIGN_IN` code and retrieves the signed-in account from the intent data.
     * If the sign-in is successful, it calls `firebaseAuthWithGoogle()` to authenticate with Firebase using
     * the Google ID token. If an error occurs (such as an `ApiException`), the `onFailure` callback is invoked.
     *
     * @param requestCode The request code passed to identify the sign-in intent result, expected to be `RC_SIGN_IN`.
     * @param data The intent data returned from the Google Sign-In activity containing the sign-in result.
     * @param onSuccess A callback function to be executed if the Google Sign-In and Firebase authentication succeed.
     * @param onFailure A callback function to be executed if the sign-in or Firebase authentication fails.
     *
     */
    fun handleGoogleSignInResult(requestCode: Int, data: Intent?, onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    firebaseAuthWithGoogle(it.idToken!!, onSuccess, onFailure)
                }
            } catch (e: ApiException) {
                onFailure()
                throw e
            }
        }
    }

    /**
     * Authenticates the user with Firebase using a Google ID token.
     *
     * This private method takes the Google ID token obtained after a successful Google Sign-In and uses it
     * to authenticate the user with Firebase. It creates a Firebase credential using the `GoogleAuthProvider`,
     * and then attempts to sign in to Firebase with that credential.
     *
     * Upon successful authentication, the `onSuccess` callback is triggered. If the authentication fails,
     * a toast message is displayed to notify the user of the failure, and the `onFailure` callback is invoked.
     *
     * @param idToken The Google ID token obtained from the Google Sign-In result.
     * @param onSuccess A callback function to be executed if Firebase authentication succeeds.
     * @param onFailure A callback function to be executed if Firebase authentication fails.
     */
    private fun firebaseAuthWithGoogle(idToken: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(context) { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    Toast.makeText(context, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                    onFailure()
                }
            }
    }

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
     */
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<Boolean> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(authResult.user != null)
        } catch(e: FirebaseAuthException) {
            Result.failure(Exception("User already registered. Please try logging in."))
        }catch (e: Exception) {
            Result.failure(Exception("An error occurred in log in. Please try again later."))
        }
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
     */
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<Boolean> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(authResult.user != null)
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(Exception("User not found."))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(Exception("Invalid email or password"))
        } catch (e: Exception) {
            Result.failure(Exception("An error occurred in log in. Please try again later."))
        }
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

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
