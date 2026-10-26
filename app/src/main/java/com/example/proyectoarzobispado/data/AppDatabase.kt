package com.example.proyectoarzobispado.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UsuarioEntity::class, RecintoEntity::class, RutaEntity::class, ParadaEntity::class, FavoritoEntity::class, AvisoEntity::class, VisitaEntity::class],
    version = 4,
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
        @Volatile private var instancia: AppDatabase? = null

        fun obtener(context: Context): AppDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "red_patrimonial.db")
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build().also { instancia = it }
            }
    }
}
