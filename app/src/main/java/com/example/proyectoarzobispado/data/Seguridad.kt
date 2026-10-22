package com.example.proyectoarzobispado.data

import java.security.MessageDigest
import java.security.SecureRandom

object Seguridad {
    fun generarSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun hash(clave: String, salt: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest((salt + clave).toByteArray())
            .joinToString("") { "%02x".format(it) }
}
