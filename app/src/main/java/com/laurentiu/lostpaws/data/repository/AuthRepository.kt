package com.laurentiu.lostpaws.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.laurentiu.lostpaws.data.local.dao.UserDao
import com.laurentiu.lostpaws.data.local.entity.UserEntity
import com.laurentiu.lostpaws.data.session.SessionManager
import java.security.MessageDigest

data class AuthResult(
    val success: Boolean,
    val message: String? = null,
    val user: UserEntity? = null
)

class AuthRepository(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) {
    suspend fun register(fullName: String, email: String, password: String): AuthResult {
        val validationError = validateRegister(fullName, email, password)
        if (validationError != null) return AuthResult(false, validationError)

        val normalizedEmail = email.trim().lowercase()
        if (userDao.findUserByEmail(normalizedEmail) != null) {
            return AuthResult(false, "Exista deja un cont cu acest email.")
        }

        return try {
            val user = UserEntity(
                fullName = fullName.trim(),
                email = normalizedEmail,
                password = hashPassword(password),
                createdAt = System.currentTimeMillis()
            )
            val id = userDao.insertUser(user)
            val savedUser = user.copy(id = id)
            sessionManager.saveSession(id, savedUser.email, savedUser.fullName)
            AuthResult(true, user = savedUser)
        } catch (_: SQLiteConstraintException) {
            AuthResult(false, "Exista deja un cont cu acest email.")
        } catch (_: Exception) {
            AuthResult(false, "Nu s-a putut crea contul. Incearca din nou.")
        }
    }

    suspend fun login(email: String, password: String): AuthResult {
        val validationError = validateLogin(email, password)
        if (validationError != null) return AuthResult(false, validationError)

        val user = userDao.findUserByEmailAndPassword(
            email.trim().lowercase(),
            hashPassword(password)
        )

        return if (user == null) {
            AuthResult(false, "Email sau parola incorecta.")
        } else {
            sessionManager.saveSession(user.id, user.email, user.fullName)
            AuthResult(true, user = user)
        }
    }

    fun logout() {
        sessionManager.clearSession()
    }

    private fun validateRegister(fullName: String, email: String, password: String): String? {
        return when {
            fullName.trim().length < 3 -> "Numele trebuie sa aiba minimum 3 caractere."
            !isValidEmail(email) -> "Introdu un email valid."
            password.length < 6 -> "Parola trebuie sa aiba minimum 6 caractere."
            else -> null
        }
    }

    private fun validateLogin(email: String, password: String): String? {
        return when {
            !isValidEmail(email) -> "Introdu un email valid."
            password.isBlank() -> "Introdu parola."
            else -> null
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val trimmed = email.trim()
        return trimmed.contains("@") && trimmed.contains(".") && trimmed.length >= 5
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString(separator = "") { "%02x".format(it) }
    }
}
