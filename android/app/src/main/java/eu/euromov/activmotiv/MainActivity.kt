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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                Surface {
                    val am = AccountManager.get(applicationContext)
                    val accounts: Array<Account> =
                        am.getAccountsByType(applicationContext.getString(R.string.accountType))

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
                    } else {
                        val account = am.accounts[0]
                        Main(account)
                    }
                }
            }
        }
    }
}

sealed class Screen(val route: String, val resourceId: Int, val icon: ImageVector) {
    object Profile : Screen("profile", R.string.profile, Icons.Filled.AccountBox)
    object Stats : Screen("stats", R.string.stats, Icons.Filled.List)
    object Settings : Screen("settings", R.string.settings, Icons.Filled.Settings)
}

@Composable
fun Main(account: Account) {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf(Screen.Profile, Screen.Stats, Screen.Settings)

    Column {
        NavHost(navController, startDestination = Screen.Profile.route, modifier = Modifier.weight(1f)) {
            composable(Screen.Profile.route) { Hello(account) }
            composable(Screen.Stats.route) { Stats() }
            composable(Screen.Settings.route) { Settings() }
        }
        NavigationBar {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = stringResource(id = item.resourceId)) },
                    label = { Text(stringResource(id = item.resourceId)) },
                    selected = selectedItem == index,
                    onClick = {
                        selectedItem = index
                        navController.navigate(item.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }

                    }
                )
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
            verticalArrangement = Arrangement.spacedBy(20.dp),
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
fun Stats() {

}

@Composable
fun Settings() {

}

@Composable
fun FirstTime(onSelect: (Boolean) -> Unit) {
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
                "ActivMotiv",
                fontFamily = FontFamily(Font(R.font.bulorounded_black)),
                fontSize = 40.sp
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