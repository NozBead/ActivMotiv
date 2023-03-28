package eu.euromov.activmotiv.data.client.auth

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.content.Context
import android.os.Bundle

class ClientAuthentificator(context: Context) : AbstractAccountAuthenticator(context) {

    override fun addAccount(response: AccountAuthenticatorResponse, accountType: String, authTokenType: String, requiredFeatures: Array<String>, options: Bundle) : Bundle {
        return Bundle()
    }

    override fun confirmCredentials(response: AccountAuthenticatorResponse, account: Account, options: Bundle) : Bundle {
        return Bundle()
    }
    
    override fun updateCredentials(response: AccountAuthenticatorResponse, account: Account, authTokenType: String, options: Bundle) : Bundle {
        return Bundle()
    }


    override fun editProperties(response: AccountAuthenticatorResponse, accountType: String) : Bundle {
        return Bundle()
    }

    override fun getAuthToken(response: AccountAuthenticatorResponse, account: Account, authTokenType: String, options: Bundle) : Bundle {
        return Bundle()
    }

    override fun getAuthTokenLabel(authTokenType: String) : String {
        return ""
    }

    override fun hasFeatures(response: AccountAuthenticatorResponse, account: Account, features: Array<String>) : Bundle {
        return Bundle()
    }
}