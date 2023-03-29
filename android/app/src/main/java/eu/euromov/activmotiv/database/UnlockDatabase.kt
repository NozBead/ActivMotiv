package eu.euromov.activmotiv.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import eu.euromov.activmotiv.model.Converters
import eu.euromov.activmotiv.model.Unlock

@Database(entities = [Unlock::class], version = 1)
@TypeConverters(Converters::class)
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
}
