package eu.euromov.activmotiv.popup

import android.content.Context
import androidx.compose.ui.text.toLowerCase
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import eu.euromov.activmotiv.R
import eu.euromov.activmotiv.client.UploadClient
import eu.euromov.activmotiv.database.UnlockDatabase
import eu.euromov.activmotiv.model.Image
import eu.euromov.activmotiv.model.ImageType
import java.io.File

class SyncImageWorker (private val appContext: Context, workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {
    private val client = UploadClient.getClient(appContext.getString(R.string.server))
    private val database = UnlockDatabase.getDatabase(appContext)

    fun download(type : ImageType) : Boolean {
        val images = database.imageDao()
        val typeStr = type.toString().lowercase()

        val response = client.getImages(typeStr).execute()
        if (!response.isSuccessful) {
            return false
        }

        response.body()?.photos?.forEach {
            if (!images.exists(it)) {
                val imageResponse = client.getImage(typeStr, it).execute()
                if (imageResponse.isSuccessful) {
                    val body = imageResponse.body()
                    if (body != null) {
                        val img = Image(it, body.bytes(), type, false, null)
                        images.insert(img)
                    }
                }
            }
        }

        return true
    }

    override suspend fun doWork(): Result {
        val sport = download(ImageType.SPORT);
        val positive = download(ImageType.POSITIVE);

        if (!sport || !positive) {
            return Result.failure()
        }

        return Result.success()
    }
}