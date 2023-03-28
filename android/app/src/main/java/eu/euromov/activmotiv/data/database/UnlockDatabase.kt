package eu.euromov.activmotiv.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import eu.euromov.activmotiv.data.dao.UnlockDao
import eu.euromov.activmotiv.data.model.Converters
import eu.euromov.activmotiv.data.model.Unlock

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
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
    abstract fun unlockDao(): UnlockDao
}
