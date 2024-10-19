package br.com.ilstudio.dermatologyapp.data.service

import br.com.ilstudio.dermatologyapp.data.model.user.UserData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()

    suspend fun saveUser(uid: String, user: UserData): Void? {
        return db.collection("users").document(uid)
            .set(user)
            .await()
    }

    suspend fun getUser(uid: String): DocumentSnapshot? {
        return db.collection("users")
            .document(uid)
            .get()
            .await()
    }
}
