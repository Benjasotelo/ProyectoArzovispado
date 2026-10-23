package com.example.proyectoarzobispado.data

import android.content.Context
import com.example.proyectoarzobispado.model.EstadoRecinto
import com.example.proyectoarzobispado.model.Recinto
import com.example.proyectoarzobispado.model.RecintosEjemplo
import com.example.proyectoarzobispado.model.Rol
import com.example.proyectoarzobispado.model.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Repositorio private constructor(context: Context) {

    private val db = AppDatabase.obtener(context)
    private val sesion = SessionManager(context)

    // ---------- Recintos ----------
    val recintos: Flow<List<Recinto>> =
        db.recintoDao().observarTodos().map { lista -> lista.map { it.aModelo() } }
    suspend fun actualizarEstado(idRecinto: Int, estado: EstadoRecinto) {
        db.recintoDao().actualizarEstado(idRecinto, estado.name)
    }

    // ---------- Datos iniciales (se ejecuta al abrir la app) ----------
    suspend fun sembrarDatosIniciales() {
        // IGNORE: solo inserta los recintos que aún no existen
        db.recintoDao().insertarTodos(RecintosEjemplo.lista.map { it.aEntidad() })

        if (db.usuarioDao().contar() == 0) {
            crearUsuario("Juan Pérez", "juanperez@duocuc.cl", "123456", Rol.TURISTA)
            crearUsuario("Colegio Demo", "colegio@demo.cl", "123456", Rol.COLEGIO)
            crearUsuario("Encargado Demo", "encargado@demo.cl", "123456", Rol.ENCARGADO)
        }
    }

    // ---------- Sesión ----------
    fun haySesion(): Boolean = sesion.usuarioId() != -1L

    suspend fun usuarioActual(): Usuario? {
        val id = sesion.usuarioId()
        if (id == -1L) return null
        return db.usuarioDao().buscarPorId(id)?.aModelo()
    }

    fun cerrarSesion() = sesion.cerrar()

    // ---------- Login y registro ----------
    suspend fun iniciarSesion(email: String, clave: String): Boolean {
        val usuario = db.usuarioDao().buscarPorEmail(email.trim().lowercase()) ?: return false
        if (!usuario.activo) return false
        if (Seguridad.hash(clave, usuario.salt) != usuario.passwordHash) return false
        sesion.guardar(usuario.id)
        return true
    }

    /** Devuelve null si todo salió bien, o el mensaje de error. */
    suspend fun registrar(nombre: String, email: String, clave: String): String? {
        val correo = email.trim().lowercase()
        if (db.usuarioDao().buscarPorEmail(correo) != null) {
            return "Ya existe una cuenta con ese correo"
        }
        val id = crearUsuario(nombre.trim(), correo, clave, Rol.TURISTA)
        if (id == -1L) return "No se pudo crear la cuenta"
        sesion.guardar(id)
        return null
    }

    private suspend fun crearUsuario(nombre: String, email: String, clave: String, rol: Rol): Long {
        val salt = Seguridad.generarSalt()
        return db.usuarioDao().insertar(
            UsuarioEntity(
                nombre = nombre,
                email = email,
                salt = salt,
                passwordHash = Seguridad.hash(clave, salt),
                rol = rol.name
            )
        )
    }

    companion object {
        @Volatile private var instancia: Repositorio? = null

        fun obtener(context: Context): Repositorio =
            instancia ?: synchronized(this) {
                instancia ?: Repositorio(context.applicationContext).also { instancia = it }
            }
    }
}
