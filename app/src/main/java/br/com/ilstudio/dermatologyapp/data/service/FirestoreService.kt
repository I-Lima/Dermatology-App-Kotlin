package br.com.ilstudio.dermatologyapp.data.service

import br.com.ilstudio.dermatologyapp.data.model.user.NewAccount
import br.com.ilstudio.dermatologyapp.data.model.user.UserData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()

    internal suspend fun saveUser(uid: String, user: UserData): Void? {
        return db.collection(USERS_TABLE).document(uid)
            .set(user)
            .await()
    }

    internal suspend fun getUser(uid: String): DocumentSnapshot? {
        return db.collection(USERS_TABLE)
            .document(uid)
            .get()
            .await()
    }

    internal suspend fun updateUser(user: UserData): Void? {
        return db.collection(USERS_TABLE).document(user.uid)
            .update(user.toMapUpdate())
            .await()
    }

    internal suspend fun updateGoogleAccount(user: NewAccount): Void? {
        return db.collection(USERS_TABLE).document(user.uid)
            .update(user.toMap())
            .await()
    }

    internal suspend fun deleteUser(uid: String): Void {
        return db.collection(USERS_TABLE).document(uid)
            .delete()
            .await()
    }

    companion object {
        private const val USERS_TABLE = "users"
    }
}
