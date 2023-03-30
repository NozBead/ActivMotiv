package eu.euromov.activmotiv

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AuthenticatorException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import eu.euromov.activmotiv.ui.theme.ActivMotivTheme

class MainActivity : ComponentActivity() {

    private fun addAccount(connected : MutableState<Boolean>, register : Boolean) {
        val am = AccountManager.get(applicationContext)
        val options = Bundle()
        options.putBoolean(applicationContext.getString(R.string.register_option), register)
        am.addAccount(
            resources.getString(R.string.accountType),
            null,
            null,
            options,
            this,
            {
               try {
                   it.result
                   connected.value = false
               } catch (e : AuthenticatorException) {
                   Toast.makeText(applicationContext, "Erreur lors de l'ajout du compte", Toast.LENGTH_SHORT).show()
               }
            } ,
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

                val connected = rememberSaveable { mutableStateOf(accounts.isEmpty()) }

                if (connected.value) {
                    FirstTime(connected, ::addAccount)
                }
                else {
                    val account = am.accounts[0]
                    Hello(account.name)
                }
            }
        }
    }
}

@Composable
fun Hello(name : String) {
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
                painter = painterResource(id = R.drawable.logo),
                "Logo"
            )
            Text(
                fontSize = 30.sp,
                text = "Bonjour $name")
        }
    }
}
@Composable
fun FirstTime(connected: MutableState<Boolean>, add: (MutableState<Boolean>, Boolean) -> Unit) {
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
                onClick = {add(connected, true)}
            ) {
                Text("Créer un compte")
            }
            Button(
                onClick = {add(connected, false)}
            ) {
                Text("Se connecter")
            }
        }
    }
}