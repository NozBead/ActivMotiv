package eu.euromov.activmotiv.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import eu.euromov.activmotiv.model.Image
import eu.euromov.activmotiv.model.Unlock

@Database(
    entities = [Unlock::class, Image::class],
    version = 2,
    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ]
)
abstract class UnlockDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: UnlockDatabase? = null

        fun getDatabase(context: Context): UnlockDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UnlockDatabase::class.java,
                    "unlock_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
    abstract fun unlockDao(): UnlockDao
    abstract fun imageDao(): ImageDao
}
