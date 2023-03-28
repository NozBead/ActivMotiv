package eu.euromov.activmotiv.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import eu.euromov.activmotiv.data.database.UnlockDatabase
import eu.euromov.activmotiv.data.model.Unlock

class SaveWorker (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val unlock = inputData.keyValueMap["Unlock"] ?: return Result.failure()
        UnlockDatabase.getDatabase(applicationContext).unlockDao().insert(unlock as Unlock)
        return Result.success()
    }
}