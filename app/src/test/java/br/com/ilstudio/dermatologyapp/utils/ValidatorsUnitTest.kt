package br.com.ilstudio.dermatologyapp.utils

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ValidatorsUnitTest {
    @Test
    fun `Should isValidEmail return true to valid email`() {
        assertTrue(Validators.isValidEmail("mestre.supremo@example.com"))
        assertTrue(Validators.isValidEmail("user123@domain.co"))
        assertTrue(Validators.isValidEmail("john_doe99@my-mail.org"))
    }

    @Test
    fun `Should isValidEmail return false to invalid email`() {
        assertFalse(Validators.isValidEmail("mestre.supremo@"))
        assertFalse(Validators.isValidEmail("@example.com"))
        assertFalse(Validators.isValidEmail("invalidemail.com"))
        assertFalse(Validators.isValidEmail("user@domain"))
    }

    @Test
    fun `Should isValidPassword return false to invalid password`() {
        assertFalse(Validators.isValidPassword("abc"))
        assertFalse(Validators.isValidPassword("Abcdefg!"))
    }

    @Test
    fun `Should isValidPassword return true to valid password without special characters`() {
        assertTrue(Validators.isValidPassword("Abcdefgh"))
        assertTrue(Validators.isValidPassword("passwordSemEspecial"))
    }

    @Test
    fun `Should isValidDate return Valid to major user`() {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val adultBirthDate = LocalDate.now().minusYears(25).format(formatter)

        assertEquals("Valid", Validators.isValidDate(adultBirthDate))
    }

    @Test
    fun `Should isValidDate return Minor to minor user`() {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val minorBirthDate = LocalDate.now().minusYears(10).format(formatter)

        assertEquals("Minor", Validators.isValidDate(minorBirthDate))
    }

    @Test
    fun `Should isValidDate return Invalid to future date`() {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val futureDate = LocalDate.now().plusDays(10).format(formatter)

        assertEquals("Invalid", Validators.isValidDate(futureDate))
    }

    @Test
    fun `Should isValidDate return error to invalid date format`() {
        assertEquals("error", Validators.isValidDate("2025-10-10"))
        assertEquals("error", Validators.isValidDate("31-12-2000"))
        assertEquals("error", Validators.isValidDate("abc"))
    }
}
