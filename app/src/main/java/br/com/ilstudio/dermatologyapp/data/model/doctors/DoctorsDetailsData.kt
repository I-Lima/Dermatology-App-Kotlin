package br.com.ilstudio.dermatologyapp.data.model.doctors

data class DoctorsDetailsData (
    val id: String,
    val starts: Long,
    val profile: String,
    val highlights: String,
    val focus: String,
    val experience: Long,
    val date: String,
    val careerPath: String,
    val comments: Long,
)