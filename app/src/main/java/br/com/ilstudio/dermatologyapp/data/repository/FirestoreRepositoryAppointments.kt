package br.com.ilstudio.dermatologyapp.data.repository

import br.com.ilstudio.dermatologyapp.data.model.appointments.AppointmentsData
import br.com.ilstudio.dermatologyapp.data.model.appointments.AppointmentsResponse
import br.com.ilstudio.dermatologyapp.data.service.FirestoreServiceAppointments
import br.com.ilstudio.dermatologyapp.domain.model.Appointment
import com.google.firebase.Timestamp
import java.time.LocalDate

class FirestoreRepositoryAppointments {
    private val firestoreServiceAppointments = FirestoreServiceAppointments()

    suspend fun getAppointmentsByDoctorId(
        doctorId: String,
        selectedDate: LocalDate
    ): AppointmentsResponse {
        return try {
            val documents = firestoreServiceAppointments
                .getAppointmentsByDoctorId(doctorId, selectedDate)
                .documents

            if (documents.isEmpty()) {
                return AppointmentsResponse(false, null, null, true)
            }

            val listData = documents.map { doc ->
                val data = doc.data.orEmpty()

                AppointmentsData(
                    uid         = data["uid"] as? String ?: "",
                    doctorUid   = data["doctor_uid"] as? String ?: "",
                    userUid     = data["user_uid"] as? String ?: "",
                    servicesUid = data["services_uid"] as? String ?: "",
                    startTime   = data["start_time"] as? Timestamp ?: Timestamp.now(),
                    endTime     = data["end_time"] as? Timestamp ?: Timestamp.now(),
                    description = data["description"] as? String ?: "",
                    status      = data["status"] as? Int ?: 0
                )
            }

            AppointmentsResponse(true, listData, null)
        } catch (e: Exception) {
            AppointmentsResponse(false, null, e.message ?: "Get appointments error")
        }
    }

    suspend fun getAppointmentsByUserId(userId: String) {
        firestoreServiceAppointments.getAppointmentsByUserId(userId)
    }

    suspend fun createAppointment(data: Appointment): AppointmentsResponse {
        return try {
            val response = firestoreServiceAppointments.createAppointment(data)

            println(response);

            //TODO: Add the logic

            AppointmentsResponse(true, null, null, false)
        } catch (e: Exception) {
            AppointmentsResponse(false, null, "Set appointments error")
        }
    }
}
