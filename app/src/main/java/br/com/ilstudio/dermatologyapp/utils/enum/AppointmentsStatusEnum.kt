package br.com.ilstudio.dermatologyapp.utils.enum

enum class AppointmentsStatusEnum (val code: String, val note: String) {
    SCHEDULED("0", "scheduled"),
    ATTEND("1", "attend"),
    CANCELED("2", "canceled"),
    ABSENCE("3", "absence");

    override fun toString(): String {
        return code
    }

    companion object {
        fun fromCode(code: String): AppointmentsStatusEnum? {
            return AppointmentsStatusEnum.values().find { it.code == code }
        }
    }
}
