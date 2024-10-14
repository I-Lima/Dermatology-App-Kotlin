package br.com.ilstudio.dermatologyapp.utils

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
}