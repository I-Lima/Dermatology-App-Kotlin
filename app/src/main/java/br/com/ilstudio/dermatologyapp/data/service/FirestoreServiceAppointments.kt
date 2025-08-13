package br.com.ilstudio.dermatologyapp.data.service

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class FirestoreServiceAppointments {
    private val db = FirebaseFirestore.getInstance()

    internal suspend fun getAppointmentsByDoctorId(doctorId: String): QuerySnapshot {
        return db.collection(APPOINTMENTS_TABLE)
            .whereEqualTo("doctor_uid", doctorId)
            .get()
            .await()
    }

    internal suspend fun getAppointmentsByUserId(userId: String): QuerySnapshot {
        return db.collection(APPOINTMENTS_TABLE)
            .whereEqualTo("user_uid", userId)
            .get()
            .await()
    }

    companion object {
        private const val APPOINTMENTS_TABLE = "appointments"
    }
}