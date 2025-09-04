package br.com.ilstudio.dermatologyapp.domain.model

import com.google.firebase.Timestamp

data class Appointment(
    var doctorUid: String = "",
    var userUid: String = "",
    var servicesUid: String = "",
    var startTime: Timestamp = Timestamp.now(),
    var endTime: Timestamp = Timestamp.now(),
    val description: String = "",
    val status: Int = 0
)
