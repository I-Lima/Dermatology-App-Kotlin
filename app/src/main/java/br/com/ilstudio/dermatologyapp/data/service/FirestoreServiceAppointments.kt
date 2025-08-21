package br.com.ilstudio.dermatologyapp.data.service

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class FirestoreServiceAppointments {
    private val db = FirebaseFirestore.getInstance()

    internal suspend fun getAppointmentsByDoctorId(doctorId: String, selectedDate: LocalDate): QuerySnapshot {
        val startOfDay = selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
        val endOfDay = selectedDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()

        return db.collection("appointments")
            .whereEqualTo("doctor_uid", doctorId)
            .whereGreaterThanOrEqualTo("start_time", Timestamp(Date.from(startOfDay)))
            .whereLessThan("start_date", Timestamp(Date.from(endOfDay)))
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