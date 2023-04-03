package eu.euromov.activmotiv

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerFuture
import android.accounts.AuthenticatorException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import eu.euromov.activmotiv.ui.theme.ActivMotivTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private fun addAccount(register : Boolean) : AccountManagerFuture<Bundle> {
        val am = AccountManager.get(applicationContext)
        val options = Bundle()
        options.putBoolean(applicationContext.getString(R.string.register_option), register)
        return am.addAccount(
            resources.getString(R.string.accountType),
            null,
            null,
            options,
            this,
            null,
            null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val serviceIntent = Intent(applicationContext, MainService::class.java)
        applicationContext.startForegroundService(serviceIntent)

        setContent {
            ActivMotivTheme {
                val am = AccountManager.get(applicationContext)
                val accounts: Array<Account> = am.getAccountsByType(applicationContext.getString(R.string.accountType))

                var connected by rememberSaveable { mutableStateOf(accounts.isNotEmpty()) }

                if (!connected) {
                    FirstTime {
                        val addJob = lifecycleScope.async(Dispatchers.IO) {
                            addAccount(it).result
                        }
                        lifecycleScope.launch(Dispatchers.Main) {
                            try {
                                addJob.await()
                                connected = true
                            } catch (e: AuthenticatorException) {
                                Log.e("Add account", e.stackTraceToString())
                            }
                        }
                    }
                }
                else {
                    val account = am.accounts[0]
                    Hello(account)
                }
            }
        }
    }
}

@Composable
fun Hello(account : Account) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                "Logo"
            )
            Text(
                fontSize = 30.sp,
                text = "Bonjour "  + account.name)
        }
    }
}
@Composable
fun FirstTime(onSelect: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.full_logo),
                "Logo"
            )
            Button(
                onClick = {onSelect(true)}
            ) {
                Text("Créer un compte")
            }
            Button(
                onClick = {onSelect(false)}
            ) {
                Text("Se connecter")
            }
        }
    }
}