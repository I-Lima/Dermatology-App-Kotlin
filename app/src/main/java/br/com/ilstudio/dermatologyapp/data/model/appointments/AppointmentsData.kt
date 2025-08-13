package br.com.ilstudio.dermatologyapp.data.model.appointments

import br.com.ilstudio.dermatologyapp.utils.enum.AppointmentsStatusEnum
import java.sql.Timestamp

class AppointmentsData(
    val uid: String,
    val doctor_uid: String,
    val user_uid: String,
    val services_uid: String,
    val start_time: Timestamp,
    val end_time: Timestamp,
    val description: String,
    val status: String,
)
