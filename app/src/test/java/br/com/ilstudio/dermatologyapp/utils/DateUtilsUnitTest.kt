package br.com.ilstudio.dermatologyapp.utils

import com.google.firebase.Timestamp as FirebaseTimestamp
import org.junit.Assert.*
import org.junit.Test
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateUtilsUnitTest {

    @Test
    fun `Should dateToTimestamp convert string to Timestamp`() {
        val input = "10/10/2020"
        val timestamp = DateUtils.dateToTimestamp(input)

        val expected = SimpleDateFormat("dd/MM/yyyy").parse(input)?.time
        assertEquals(expected, timestamp.time)
    }

    @Test
    fun `Should localDate return current date in format yyyy-MM-dd HH mm ss SSS`() {
        val result = DateUtils.localDate()
        assertTrue(result.matches(Regex("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}")))
    }

    @Test
    fun `Should timestampToDate convert Timestamp to string`() {
        val date = SimpleDateFormat("dd/MM/yyyy").parse("01/01/2024")
        val timestamp = Timestamp(date.time)

        val formatted = DateUtils.timestampToDate(timestamp)
        assertEquals("01/01/2024", formatted)
    }

    @Test
    fun `Should timestampToDate return null for null Timestamp`() {
        assertNull(DateUtils.timestampToDate(null))
    }

    @Test
    fun `Should timestampToHourString convert FirebaseTimestamp to string`() {
        val date = Date.from(LocalDateTime.of(2024, 1, 1, 15, 30).atZone(java.time.ZoneId.systemDefault()).toInstant())
        val timestamp = FirebaseTimestamp(date)

        val result = DateUtils.timestampToHourString(timestamp)
        assertEquals("15:30", result)
    }

    @Test
    fun `Should getCurrentWeekDays return week days`() {
        val days = DateUtils.getCurrentWeekDays()
        assertEquals(5, days.size)
        assertTrue(days.all { it.day.isNotEmpty() })
        assertTrue(days.all { it.date.isNotEmpty() })
    }

    @Test
    fun `Should addWeekDays return next week days`() {
        val today = LocalDate.of(2024, 5, 6) // Segunda-feira
        val nextWeekDays = DateUtils.addWeekDays(today)

        assertEquals(5, nextWeekDays.size)
        assertTrue(nextWeekDays.first().searchDate.isAfter(today))
    }

    @Test
    fun `Should subtractWeekDays return previous week days`() {
        val today = LocalDate.of(2024, 5, 6)
        val prevWeekDays = DateUtils.subtractWeekDays(today)

        assertEquals(5, prevWeekDays.size)
        assertTrue(prevWeekDays.first().searchDate.isBefore(today))
    }

    @Test
    fun `Should toTimestamp convert date and time to FirebaseTimestamp`() {
        val date = "2024-01-01"
        val time = "12:30"

        val result = DateUtils.toTimestamp(date, time)

        val expectedEpochSeconds = LocalDateTime.of(2024, 1, 1, 12, 30)
            .atZone(java.time.ZoneId.systemDefault()).toInstant().epochSecond

        assertEquals(expectedEpochSeconds, result.seconds)
    }

    @Test
    fun `Should addMinutesIntoTimestamp sum minutes to FirebaseTimestamp`() {
        val date = Date.from(LocalDateTime.of(2024, 1, 1, 12, 0)
            .atZone(java.time.ZoneId.systemDefault()).toInstant())
        val timestamp = FirebaseTimestamp(date)

        val newTimestamp = DateUtils.addMinutesIntoTimestamp(timestamp, 30)

        val diff = (newTimestamp.toDate().time - timestamp.toDate().time) / 1000 / 60
        assertEquals(30, diff)
    }

    @Test
    fun `Should timestampToAge calculate age from timestamp`() {
        val birthDate = LocalDate.now().minusYears(25).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val age = DateUtils.timestampToAge(birthDate)

        assertEquals(25, age)
    }

    @Test
    fun `Should timestampToAge return 0 for invalid timestamp`() {
        assertEquals(0, DateUtils.timestampToAge(null))
        assertEquals(0, DateUtils.timestampToAge("data_invalida"))
    }
}
