package br.com.ilstudio.dermatologyapp.data.model.appointments

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class AppointmentsData(
    val uid: String,
    @get:PropertyName("doctor_uid") @set:PropertyName("doctor_uid")
    var doctorUid: String = "",
    @get:PropertyName("user_uid") @set:PropertyName("user_uid")
    var userUid: String? = "",
    @get:PropertyName("services_uid") @set:PropertyName("services_uid")
    var servicesUid: String = "",
    @get:PropertyName("start_time") @set:PropertyName("start_time")
    var startTime: Timestamp = Timestamp.now(),
    @get:PropertyName("end_time") @set:PropertyName("end_time")
    var endTime: Timestamp = Timestamp.now(),
    val description: String = "",
    val status: Int = 0,
    @get:PropertyName("full_name") @set:PropertyName("full_name")
    var fullName: String? = null,
    val gender: String? = null,
    val age: Timestamp? = null,
)

