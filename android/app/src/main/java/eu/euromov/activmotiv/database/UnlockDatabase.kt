package eu.euromov.activmotiv.database

import androidx.room.Database
import androidx.room.RoomDatabase
import eu.euromov.activmotiv.dao.UnlockDao
import eu.euromov.activmotiv.model.Unlock

@Database(entities = [Unlock::class], version = 1)
abstract class UnlockDatabase : RoomDatabase() {
    abstract fun unlockDao(): UnlockDao
}
