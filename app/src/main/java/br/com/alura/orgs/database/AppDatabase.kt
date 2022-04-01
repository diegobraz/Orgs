package br.com.alura.orgs.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import br.com.alura.orgs.database.converter.Converters
import br.com.alura.orgs.database.dao.ProdutoDao
import br.com.alura.orgs.database.dao.UsuarioDao
import br.com.alura.orgs.model.Produto
import br.com.alura.orgs.model.Usuario

@Database(
    entities = [
        Produto::class,
        Usuario::class
    ],
    version = 2,
    autoMigrations = [AutoMigration(from = 1 , to = 2, spec = AppDatabase.MyAutoMigration::class)],
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    class MyAutoMigration : AutoMigrationSpec

    abstract fun produtoDao(): ProdutoDao
    abstract fun usuarioDao():UsuarioDao

    companion object {
        @Volatile
        private var db: AppDatabase? = null
        fun instancia(context: Context): AppDatabase {
            return db ?: Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "orgs.db"
            ).build().also {
                db = it
            }
        }
    }
}