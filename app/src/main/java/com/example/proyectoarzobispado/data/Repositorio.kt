package com.example.proyectoarzobispado.data

import android.content.Context
import com.example.proyectoarzobispado.model.EstadoRecinto
import com.example.proyectoarzobispado.model.Recinto
import com.example.proyectoarzobispado.model.RecintosEjemplo
import com.example.proyectoarzobispado.model.Rol
import com.example.proyectoarzobispado.model.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.room.withTransaction
import com.example.proyectoarzobispado.model.Ruta
import kotlinx.coroutines.flow.combine
import com.example.proyectoarzobispado.model.Aviso
import com.example.proyectoarzobispado.model.EstadoVisita
import com.example.proyectoarzobispado.model.Visita
import com.example.proyectoarzobispado.model.Evento
import com.example.proyectoarzobispado.model.EventosEjemplo

class Repositorio private constructor(context: Context) {

    private val db = AppDatabase.obtener(context)
    private val sesion = SessionManager(context)

    // ---------- Recintos ----------
    val recintos: Flow<List<Recinto>> =
        db.recintoDao().observarTodos().map { lista -> lista.map { it.aModelo() } }
    suspend fun actualizarEstado(idRecinto: Int, estado: EstadoRecinto) {
        db.recintoDao().actualizarEstado(idRecinto, estado.name)
    }

    // Para que el Encargado edite la ficha del recinto
    suspend fun actualizarFicha(recinto: Recinto) {
        db.recintoDao().actualizarFicha(recinto.aEntidad())
    }

    fun recinto(id: Int): Flow<Recinto?> =
        recintos.map { lista -> lista.find { it.id == id } }

    // ---------- Rutas ----------
    val rutas: Flow<List<Ruta>> =
        db.rutaDao().observarTodas().map { lista -> lista.map { it.aModelo() } }

    // Sirve para crear y para editar (mismo id = reemplaza)
    suspend fun guardarRuta(ruta: Ruta) {
        db.withTransaction {
            val dao = db.rutaDao()
            dao.borrarParadas(ruta.id)
            dao.guardarRuta(RutaEntity(ruta.id, ruta.nombre, ruta.tiempoTotalMinutos))
            dao.insertarParadas(ruta.paradas.map {
                ParadaEntity(
                    rutaId = ruta.id,
                    recintoId = it.recintoId,
                    orden = it.orden,
                    tiempoEstimadoMinutos = it.tiempoEstimadoMinutos
                )
            })
        }
    }

    // ---------- Favoritos (por usuario; el invitado usa el id -1) ----------
    fun idsFavoritos(): Flow<List<Int>> =
        db.favoritoDao().observarIds(sesion.usuarioId())

    // Une la lista de recintos con los ids guardados y se queda con los favoritos
    fun favoritos(): Flow<List<Recinto>> =
        combine(recintos, idsFavoritos()) { lista, ids -> lista.filter { it.id in ids } }

    suspend fun alternarFavorito(recintoId: Int, esFavoritoAhora: Boolean) {
        val usuarioId = sesion.usuarioId()
        if (esFavoritoAhora) db.favoritoDao().quitar(usuarioId, recintoId)
        else db.favoritoDao().agregar(FavoritoEntity(usuarioId, recintoId))
    }

    suspend fun eliminarRuta(id: Long) = db.rutaDao().eliminar(id)

    // ---------- Avisos ----------
    val avisos: Flow<List<Aviso>> =
        db.avisoDao().observarTodos().map { lista -> lista.map { it.aModelo() } }

    fun avisosDe(recintoId: Int): Flow<List<Aviso>> =
        avisos.map { lista -> lista.filter { it.recintoId == recintoId } }

    suspend fun publicarAviso(recintoId: Int, titulo: String, mensaje: String) {
        db.avisoDao().insertar(
            AvisoEntity(
                recintoId = recintoId,
                titulo = titulo.trim(),
                mensaje = mensaje.trim(),
                fecha = System.currentTimeMillis()
            )
        )
    }

    suspend fun eliminarAviso(id: Long) = db.avisoDao().eliminar(id)

    // ---------- Eventos ----------
    // Todos los eventos (los usa "Mi recinto" del Encargado)
    val eventos: Flow<List<Evento>> =
        db.eventoDao().observarTodos().map { lista -> lista.map { it.aModelo() } }

    // Eventos de un solo recinto (los usa la ficha)
    fun eventosDe(recintoId: Int): Flow<List<Evento>> =
        db.eventoDao().observarDe(recintoId).map { lista -> lista.map { it.aModelo() } }

    suspend fun agregarEvento(
        recintoId: Int, titulo: String, descripcion: String, fecha: String, hora: String
    ) {
        db.eventoDao().insertar(
            EventoEntity(
                recintoId = recintoId,
                titulo = titulo.trim(),
                descripcion = descripcion.trim(),
                fecha = fecha.trim(),
                hora = hora.trim()
            )
        )
    }

    suspend fun eliminarEvento(id: Long) = db.eventoDao().eliminar(id)

    // ---------- Datos iniciales (se ejecuta al abrir la app) ----------
    suspend fun sembrarDatosIniciales() {
        // IGNORE: solo inserta los recintos que aún no existen
        db.recintoDao().insertarTodos(RecintosEjemplo.lista.map { it.aEntidad() })

        if (db.usuarioDao().contar() == 0) {
            crearUsuario("Juan Pérez", "juanperez@duocuc.cl", "123456", Rol.TURISTA)
            crearUsuario("Colegio Demo", "colegio@demo.cl", "123456", Rol.COLEGIO)
            crearUsuario("Encargado Demo", "encargado@demo.cl", "123456", Rol.ENCARGADO)
        }

        if (db.avisoDao().contar() == 0) {
            val ahora = System.currentTimeMillis()
            val hora = 3_600_000L
            val dao = db.avisoDao()
            dao.insertar(AvisoEntity(recintoId = 1, titulo = "Misa solemne este domingo",
                mensaje = "La Catedral estará cerrada a visitas entre las 11:00 y las 13:00.", fecha = ahora - 2 * hora))
            dao.insertar(AvisoEntity(recintoId = 2, titulo = "Visitas guiadas gratuitas",
                mensaje = "Este sábado a las 16:00 hay recorrido guiado por el claustro.", fecha = ahora - 26 * hora))
            dao.insertar(AvisoEntity(recintoId = 5, titulo = "Horario especial",
                mensaje = "La Basílica abrirá hasta las 20:00 durante esta semana.", fecha = ahora - 50 * hora))
        }

        if (db.eventoDao().contar() == 0) {
            db.eventoDao().insertarTodos(EventosEjemplo.lista.map { it.aEntidad() })
        }
    }

    // ---------- Visitas del colegio ----------
    fun visitas(): Flow<List<Visita>> =
        db.visitaDao().observarDe(sesion.usuarioId()).map { lista -> lista.map { it.aModelo() } }

    suspend fun programarVisita(
        recintoId: Int, fechaMillis: Long, hora: String,
        participantes: Int, requiereGuia: Boolean
    ) {
        db.visitaDao().insertar(
            VisitaEntity(
                usuarioId = sesion.usuarioId(),
                recintoId = recintoId,
                fechaMillis = fechaMillis,
                hora = hora,
                participantes = participantes,
                requiereGuia = requiereGuia,
                estado = EstadoVisita.PENDIENTE.name
            )
        )
    }

    suspend fun confirmarVisita(id: Long) =
        db.visitaDao().cambiarEstado(id, EstadoVisita.CONFIRMADA.name)

    suspend fun eliminarVisita(id: Long) = db.visitaDao().eliminar(id)

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

    /** Devuelve true si la cuenta existía y se cambió la clave. */
    suspend fun cambiarClave(email: String, nuevaClave: String): Boolean {
        val usuario = db.usuarioDao().buscarPorEmail(email.trim().lowercase()) ?: return false
        val salt = Seguridad.generarSalt()   // salt nuevo en cada cambio
        db.usuarioDao().cambiarClave(usuario.id, salt, Seguridad.hash(nuevaClave, salt))
        return true
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
