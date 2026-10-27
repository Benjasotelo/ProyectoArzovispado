package com.example.proyectoarzobispado.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        UsuarioEntity::class,
        RecintoEntity::class,
        RutaEntity::class,
        ParadaEntity::class,
        FavoritoEntity::class,
        AvisoEntity::class,
        VisitaEntity::class,
        EventoEntity::class
    ],
    version = 8,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun recintoDao(): RecintoDao
    abstract fun rutaDao(): RutaDao
    abstract fun favoritoDao(): FavoritoDao
    abstract fun avisoDao(): AvisoDao
    abstract fun visitaDao(): VisitaDao
    abstract fun eventoDao(): EventoDao

    companion object {
        @Volatile
        private var instancia: AppDatabase? = null

        /** Esquema igual en 4 y 6: solo actualiza el número de versión. */
        private val MIGRATION_4_6 = object : Migration(4, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Sin cambios de tablas/columnas.
            }
        }

        /** Versión 7 agrega la tabla de eventos. */
        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS eventos (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "recintoId INTEGER NOT NULL, " +
                            "titulo TEXT NOT NULL, " +
                            "descripcion TEXT NOT NULL, " +
                            "fecha TEXT NOT NULL, " +
                            "hora TEXT NOT NULL)"
                )
            }
        }

        /** Versión 8 agrega el horario (texto) a la ficha del recinto. */
        private val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE recintos ADD COLUMN horario TEXT NOT NULL DEFAULT ''"
                )
            }
        }

        fun obtener(context: Context): AppDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "red_patrimonial.db"
                )
                    .addMigrations(MIGRATION_4_6, MIGRATION_6_7, MIGRATION_7_8)
                    .build()
                    .also { instancia = it }
            }
    }
}