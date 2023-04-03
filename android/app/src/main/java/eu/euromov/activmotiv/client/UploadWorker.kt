package eu.euromov.activmotiv.client

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import eu.euromov.activmotiv.R
import eu.euromov.activmotiv.database.UnlockDatabase
import eu.euromov.activmotiv.model.Unlock

class UploadWorker (private val appContext: Context, workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {
    private val unlocks = UnlockDatabase.getDatabase(appContext).unlockDao()
    private val client = UploadClient.getClient(appContext.getString(R.string.server))
    private val am: AccountManager = AccountManager.get(appContext)

    private fun checkToken(token : String): Boolean {
        return client.check(token).execute().code() == 200
    }

    private fun getToken(account : Account): String {
        var token = am.getAuthToken(
            account,
            "",
            null,
            true,
            null,
            null
        ).result.getString(AccountManager.KEY_AUTHTOKEN, null)

        if (token == null || !checkToken(token)) {
            am.invalidateAuthToken(applicationContext.getString(R.string.accountType), token)
            token = getToken(account)
        }
        return token
    }

    private fun upload(notSent:List<Unlock>, sessionCookie: String) {
        for (unlock in notSent) {
            val response = client.unlock(sessionCookie, unlock).execute()
            if (response.code() == 200) {
                unlock.sent = true
                unlocks.update(unlock)
            }
        }
    }

    override suspend fun doWork(): Result {
        val accounts: Array<Account> = am.getAccountsByType(appContext.getString(R.string.accountType))
        if(accounts.isEmpty()) {
            return Result.failure()
        }
        val account = accounts[0]

        val notSent = unlocks.getAllNotSent()
        if(notSent.isEmpty()) {
            return Result.success()
        }

        val token = getToken(account)
        upload(notSent, token)

        return Result.success()
    }
}