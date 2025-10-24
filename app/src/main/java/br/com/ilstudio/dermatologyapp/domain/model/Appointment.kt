package br.com.ilstudio.dermatologyapp.domain.model

import com.google.firebase.Timestamp

data class Appointment(
    var doctorUid: String = "",
    var userUid: String? = "",
    var servicesUid: String = "",
    var startTime: Timestamp = Timestamp.now(),
    var endTime: Timestamp = Timestamp.now(),
    var description: String = "",
    var status: Int = 0,
    var fullName: String? = null,
    var gender: String? = null,
    var age: String? = null,
)
