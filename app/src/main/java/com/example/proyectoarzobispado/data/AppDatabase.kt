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
        VisitaEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun recintoDao(): RecintoDao
    abstract fun rutaDao(): RutaDao
    abstract fun favoritoDao(): FavoritoDao
    abstract fun avisoDao(): AvisoDao
    abstract fun visitaDao(): VisitaDao

    companion object {
        @Volatile
        private var instancia: AppDatabase? = null

        /** Esquema igual en 4 y 6: solo actualiza el número de versión. */
        private val MIGRATION_4_6 = object : Migration(4, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Sin cambios de tablas/columnas.
            }
        }

        fun obtener(context: Context): AppDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "red_patrimonial.db"
                )
                    .addMigrations(MIGRATION_4_6)
                    .build()
                    .also { instancia = it }
            }
    }
}