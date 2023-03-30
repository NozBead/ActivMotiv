package eu.euromov.activmotiv.client.auth

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import eu.euromov.activmotiv.R
import eu.euromov.activmotiv.client.ClientCallback
import eu.euromov.activmotiv.client.UploadClient
import java.util.*

class ClientAuthenticator(val context: Context) : AbstractAccountAuthenticator(context) {
    private val am: AccountManager = AccountManager.get(context)

    override fun addAccount(response: AccountAuthenticatorResponse, accountType: String, authTokenType: String?, requiredFeatures: Array<String>?, options: Bundle) : Bundle {
        val bundle = Bundle()
        val intent = Intent(context, AccountAuthActivity::class.java)
        val option = context.getString(R.string.register_option)
        intent.putExtra(option, options.getBoolean(option))
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return bundle
    }

    override fun confirmCredentials(response: AccountAuthenticatorResponse, account: Account, options: Bundle) : Bundle {
        throw UnsupportedOperationException()
    }
    
    override fun updateCredentials(response: AccountAuthenticatorResponse, account: Account, authTokenType: String, options: Bundle) : Bundle? {
        throw UnsupportedOperationException()
    }

    override fun editProperties(response: AccountAuthenticatorResponse, accountType: String) : Bundle? {
        throw UnsupportedOperationException()
    }

    private fun getTokenResponse(token: String?, account: Account) : Bundle {
        val bundle = Bundle()
        bundle.putString(AccountManager.KEY_AUTHTOKEN, token)
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name)
        bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type)
        am.setUserData(account, AccountManager.KEY_AUTHTOKEN, token)
        return bundle
    }

    private fun getBasic(account: Account) : String {
        val base = Base64.getUrlEncoder()
        val credentials = account.name + ":" + am.getPassword(account)
        return base.encodeToString(credentials.toByteArray())
    }

    override fun getAuthToken(response: AccountAuthenticatorResponse, account: Account, authTokenType: String, options: Bundle) : Bundle? {
        val client = UploadClient.getClient(context.getString(R.string.server))

        val basic = getBasic(account)

        client.login("Basic $basic").enqueue(
            ClientCallback {
                if (it.code() == 200) {
                    val bundle = getTokenResponse(it.headers().get("Set-Cookie"), account)
                    response.onResult(bundle)
                }
                else {
                    response.onError(it.code(), "Cannot get token")
                }
            })
        return null
    }

    override fun getAuthTokenLabel(authTokenType: String) : String {
        throw UnsupportedOperationException()
    }

    override fun hasFeatures(response: AccountAuthenticatorResponse, account: Account, features: Array<String>) : Bundle? {
        throw UnsupportedOperationException()
    }
}