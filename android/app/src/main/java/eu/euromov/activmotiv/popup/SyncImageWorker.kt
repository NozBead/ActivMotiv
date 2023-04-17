package eu.euromov.activmotiv.popup

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import eu.euromov.activmotiv.R
import eu.euromov.activmotiv.client.UploadClient
import java.io.File

class SyncImageWorker (private val appContext: Context, workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {
    private val client = UploadClient.getClient(appContext.getString(R.string.server))

    fun download(type : String) : Boolean {
        val dir = File(appContext.filesDir, type)
        dir.mkdir()

        val response = client.getImages(type).execute()
        if (!response.isSuccessful) {
            return false
        }

        response.body()?.photos?.forEach {
            val photo = File(dir, it)
            if (!photo.exists()) {
                photo.createNewFile()
                val imageResponse = client.getImage(type, it).execute()
                if (imageResponse.isSuccessful) {
                    val body = imageResponse.body()
                    if (body != null) {
                        val buffer = ByteArray(1024)
                        val input = body.byteStream()
                        val output = photo.outputStream();

                        var read = input.read(buffer)
                        while (read != -1) {
                            output.write(buffer, 0, read)
                            read = input.read(buffer)
                        }
                        input.close()
                        output.close()
                    }
                }
            }
        }

        return true
    }

    override suspend fun doWork(): Result {
        val sport = download("sport");
        val positive = download("positive");

        if (!sport || !positive) {
            return Result.failure()
        }

        return Result.success()
    }
}