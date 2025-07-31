package br.com.ilstudio.dermatologyapp.domain.model

data class ItemHour(
    val hour: String,
    val period: AmPm,
    val available: Boolean,
)

enum class AmPm { AM, PM }