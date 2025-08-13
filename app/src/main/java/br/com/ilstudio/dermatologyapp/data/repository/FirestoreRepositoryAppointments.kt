package br.com.ilstudio.dermatologyapp.data.repository

import br.com.ilstudio.dermatologyapp.data.model.appointments.AppointmentsData
import br.com.ilstudio.dermatologyapp.data.model.appointments.AppointmentsResponse
import br.com.ilstudio.dermatologyapp.data.service.FirestoreServiceAppointments
import java.sql.Timestamp

class FirestoreRepositoryAppointments {
    private val firestoreServiceAppointments = FirestoreServiceAppointments()

    suspend fun getAppointmentsByDoctorId(doctorId: String): AppointmentsResponse {
        return try {
            val result = firestoreServiceAppointments.getAppointmentsByDoctorId(doctorId)
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
                    data["start_time"] as? Timestamp ?: Timestamp(0),
                    data["end_time"] as? Timestamp ?: Timestamp(0),
                    data["description"] as? String ?: "",
                    data["status"] as? String ?: ""
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