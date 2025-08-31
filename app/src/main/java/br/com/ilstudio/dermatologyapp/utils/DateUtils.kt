package br.com.ilstudio.dermatologyapp.utils


import br.com.ilstudio.dermatologyapp.domain.model.CalendarItem
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object DateUtils {
    /**
     * Converts a date string in the format "dd/MM/yyyy" to a [Timestamp].
     *
     * This function parses a date string using the format `dd/MM/yyyy` and returns
     * a [Timestamp] object representing the corresponding date and time in milliseconds.
     *
     * @param date The date string in the format "dd/MM/yyyy" to be converted.
     * @return [Timestamp] representing the parsed date and time in milliseconds.
     *
     */
    fun dateToTimestamp(date: String): Timestamp {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val dateTime: Date = dateFormat.parse(date)

        return Timestamp(dateTime.time)
    }


    /**
     * Returns the current date and time as a formatted string.
     *
     * This function retrieves the current date and time using [LocalDateTime.now] and
     * formats it according to the pattern `"yyyy-MM-dd HH:mm:ss.SSS"`.
     *
     * @return A string representing the current date and time in the format `"yyyy-MM-dd HH:mm:ss.SSS"`.
     */
    fun localDate(): String {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        return now.format(formatter)
    }

    /**
     * Converts a [Timestamp] to a formatted date string.
     *
     * This function takes a [Timestamp] object and converts it to a string representation
     * in the format "dd/MM/yyyy". If the provided timestamp is `null`, the function returns `null`.
     *
     * @param timestamp The [Timestamp] to be converted to a date string.
     * @return A formatted date string in the format "dd/MM/yyyy", or `null` if the timestamp is `null`.
     */
    fun timestampToDate(timestamp: Timestamp?): String? {
        if (timestamp == null) return null

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(timestamp)
    }

    /**
     * Converts a [Timestamp] to a formatted hour string.
     *
     * @param timestamp The [Timestamp] to be converted to a hour string.
     * @return A formatted hour string in the format "HH:mm", or `null` if the timestamp is `null`.
    */
    fun timestampToHourString(timestamp: com.google.firebase.Timestamp): String {
        val dateTime = LocalDateTime.ofInstant(timestamp.toDate().toInstant(), ZoneId.systemDefault())
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    /**
     * Get the current week's days as a list of [CalendarItem] objects.
     *
     * @return A list of [CalendarItem] objects representing the current week's days.
    * */
    fun getCurrentWeekDays(): List<CalendarItem> {
        val today = LocalDate.now()
        val startOfWeek = today.with(DayOfWeek.MONDAY)
        val formatterDay = DateTimeFormatter.ofPattern("EEE")
        val formatterDate = DateTimeFormatter.ofPattern("dd")

        return (0..4).map { i ->
            val date = startOfWeek.plusDays(i.toLong())

            val type = when {
                date.isBefore(today) -> 1
                date.isEqual(today) -> 2
                else -> 0
            }

            CalendarItem(
                day = date.format(formatterDay),
                date = date.format(formatterDate),
                type = type
            )
        }
    }
}
