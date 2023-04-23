package eu.euromov.activmotiv

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerFuture
import android.accounts.AuthenticatorException
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.euromov.activmotiv.database.UnlockDatabase
import eu.euromov.activmotiv.model.Stats
import eu.euromov.activmotiv.model.UnlockDay
import eu.euromov.activmotiv.ui.theme.ActivMotivTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.ChronoField

class MainActivity : ComponentActivity() {

    private fun requestAddAccount(register: Boolean): AccountManagerFuture<Bundle> {
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
            null
        )
    }

    private fun addAccount(register: Boolean, onSuccess: () -> Unit) {
        val addJob = lifecycleScope.async(Dispatchers.IO) {
            requestAddAccount(register).result
        }
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                addJob.await()
                onSuccess()
            } catch (e: AuthenticatorException) {
                Log.e("Add account", e.stackTraceToString())
            }
        }
    }

    private fun removeAccount(account: Account) {
        val am = AccountManager.get(applicationContext)
        am.removeAccount(
            account,
            this,
            null,
            null
        )
    }

    private fun getStats(): Stats {
        val dao = UnlockDatabase.getDatabase(applicationContext).unlockDao()
        return dao.getStat()
    }

    private fun getUnlocks(): List<UnlockDay> {
        val dao = UnlockDatabase.getDatabase(applicationContext).unlockDao()
        val weekStart = LocalDate.now().with(ChronoField.DAY_OF_WEEK, 1).atStartOfDay()
        val offset = ZoneOffset.systemDefault().rules.getOffset(weekStart)
        Log.i("temps", weekStart.toEpochSecond(offset).toString())
        return dao.getUnlockByDay(weekStart.toEpochSecond(offset))
    }

    private fun getImages(): List<ImageBitmap> {
        val files = File(applicationContext.filesDir, "positive").listFiles()
        return files?.map { BitmapFactory.decodeStream(it.inputStream()).asImageBitmap() }?.toList()
            ?: listOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val serviceIntent = Intent(applicationContext, MainService::class.java)
        applicationContext.startForegroundService(serviceIntent)
        val am = AccountManager.get(applicationContext)

        setContent {
            ActivMotivTheme {
                Surface {
                    val accounts =
                        am.getAccountsByType(applicationContext.getString(R.string.accountType))
                    var connected by rememberSaveable { mutableStateOf(accounts.isNotEmpty()) }

                    if (!connected) {
                        FirstTime {
                            addAccount(it) {
                                connected = true
                            }
                        }
                    } else {
                        val account = am.accounts[0]
                        Main(
                            account,
                            this::getStats,
                            this::getUnlocks,
                            this::getImages
                        ) {
                            connected = false
                            removeAccount(account)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Main(account: Account, onGetStats: () -> Stats, onGetUnlocks: () -> List<UnlockDay>, onGetImages: () -> List<ImageBitmap>, onDisconnect: () -> Unit) {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(-1) }
    val items = listOf(Screen.Profile, Screen.Stats, Screen.Settings)

    var lastFiles = listOf<ImageBitmap>()
    Column {
        NavHost(navController, startDestination = Screen.Welcome.route, modifier = Modifier.weight(1f)) {
            val navToInfos = {navController.navigate(Screen.Infos.route)}
            composable(Screen.Infos.route) { Infos() }
            composable(Screen.Profile.route) { Profile(account, onDisconnect) }
            composable(Screen.Welcome.route) {Hello( account, navToInfos) }
            composable(Screen.Stats.route) { Stats(onGetStats, navToInfos, onGetUnlocks) }
            composable(Screen.Settings.route) {
                Settings(
                    {
                        lastFiles=onGetImages()
                        lastFiles
                    },
                    {
                        navController.navigate(Screen.ImageSettings.route + "/" + it)
                    })
            }
            composable(
                Screen.ImageSettings.route + "/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("id")
                ImageSettings(lastFiles[id!!])
            }
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
fun Hello(account : Account, onGetInfo: () -> Unit) {
    Column {
        Box(
            modifier = Modifier
                .weight(1F)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            FloatingActionButton(
                onGetInfo,
                modifier = Modifier.padding(60.dp)
            ) {
                Icon(Icons.Filled.Info, "Info")
            }
        }
        Spacer(modifier = Modifier.padding(40.dp))
        Box(
            modifier = Modifier
                .weight(2F)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
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
                    fontWeight = FontWeight.Bold,
                    text = "Bonjour " + account.name
                )
            }
        }
    }
}

@Composable
fun HeaderIcon() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            "Logo",
            modifier = Modifier.size(30.dp)
        )
        Text(
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.bulorounded_black)),
            text = "ActivMotiv"
        )
    }
}

@Composable
fun Header(content: @Composable () -> Unit) {
    Column (
        Modifier.padding(top = 20.dp, bottom = 60.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderIcon()
        content()
    }
}

@Composable
fun Page(headerContent: @Composable () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Header(headerContent)
            content()
        }
    }
}

@Composable
fun TitledPage(title: String, content: @Composable ColumnScope.() -> Unit) {
    Page(
        {
            Text(
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                text = title
            )
        },
        content
    )
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
                Text(stringResource(id = R.string.create))
            }
            Button(
                onClick = {onSelect(false)}
            ) {
                Text(stringResource(id = R.string.connect))
            }
        }
    }
}