package br.com.ilstudio.dermatologyapp.utils

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

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
}
