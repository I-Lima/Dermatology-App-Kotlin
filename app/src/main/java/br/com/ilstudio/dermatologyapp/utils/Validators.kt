package br.com.ilstudio.dermatologyapp.utils

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object Validators {
    /**
     * Validates if the given email is in the correct format.
     *
     * This function checks if the provided string follows the pattern of a valid email address.
     * The email must contain:
     * - An alphanumeric local part (with optional dots, underscores, and percentage signs).
     * - The '@' symbol.
     * - A domain part that contains alphanumeric characters and dots.
     * - A top-level domain (TLD) that is at least two characters long.
     *
     * @param email The email string to validate.
     * @return `true` if the email is valid, `false` otherwise.
     */
    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        return emailRegex.matches(email)
    }

    /**
     * Validates whether the given password meets the required security criteria.
     *
     * This function checks if the password:
     * - Contains at least one lowercase letter.
     * - Contains at least one uppercase letter.
     * - Contains at least one special character (e.g., !@#\$%^&*).
     * - Has a minimum length of 8 characters.
     *
     * @param pass The password string to validate.
     * @return `true` if the password is valid, `false` if it fails any of the conditions.
     */
    fun isValidPassword(pass: String): Boolean {
        val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^&*(),.?\":{}|<>]).+$")
        if(pass.length < 8 || regex.matches(pass)) return false

        return true
    }

    /**
     * Validates the given date string and checks if it represents a valid date,
     * if the user is at least 18 years old, or if the date is invalid or a minor.
     *
     * This function:
     * - Parses the input date string in the format "dd/MM/yyyy".
     * - Compares the date to the current date.
     * - Returns "Valid" if the date corresponds to someone who is 18 years or older.
     * - Returns "Minor" if the age is less than 18 years.
     * - Returns "Invalid" if the date is in the future.
     * - Returns "error" if the date format is incorrect or parsing fails.
     *
     * @param dateStg The date string to validate, expected in the format "dd/MM/yyyy".
     * @return A string indicating the result: "Valid", "Minor", "Invalid", or "error".
     */
    fun isValidDate(dateStg: String): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        try {
            val date = LocalDate.parse(dateStg, formatter)
            val today = LocalDate.now()

            if (date.isAfter(today)) return "Invalid"

            val age = Period.between(date, today).years
            if (age < 18) return "Minor"

            return "Valid"
        } catch (e: DateTimeParseException) {
            return "error"
        }
    }
}
