package br.com.ilstudio.dermatologyapp.data.repository

import br.com.ilstudio.dermatologyapp.data.model.appointments.AppointmentsData
import br.com.ilstudio.dermatologyapp.data.model.appointments.AppointmentsResponse
import br.com.ilstudio.dermatologyapp.data.service.FirestoreServiceAppointments
import com.google.firebase.Timestamp
import java.time.LocalDate

class FirestoreRepositoryAppointments {
    private val firestoreServiceAppointments = FirestoreServiceAppointments()

    suspend fun getAppointmentsByDoctorId(doctorId: String, selectedDate: LocalDate): AppointmentsResponse {
        return try {
            val result = firestoreServiceAppointments.getAppointmentsByDoctorId(doctorId, selectedDate)
            val response = result.documents

            if (response.isEmpty()) {
                return AppointmentsResponse(false, null, null, true)
            }

            val listData = response.map {
                val data = it.data ?: emptyMap<String, Any>()

                AppointmentsData(
                    data["uid"] as? String ?: "",
                    data["doctor_uid"] as? String ?: "",
                    data["user_uid"] as? String ?: "",
                    data["services_uid"] as? String ?: "",
                    data["start_time"] as? Timestamp ?: Timestamp.now(),
                    data["end_time"] as? Timestamp ?: Timestamp.now(),
                    data["description"] as? String ?: "",
                    data["status"] as? Int ?: 0
                )
            }

            AppointmentsResponse(true, listData, null, false)
        } catch (e: Exception) {
            AppointmentsResponse(false, null, "Get appointments error")
        }
    }

    suspend fun getAppointmentsByUserId(userId: String) {
        firestoreServiceAppointments.getAppointmentsByUserId(userId)
    }
}