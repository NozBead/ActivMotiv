package eu.euromov.activmotiv

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val resourceId: Int, val icon: ImageVector) {
    object Infos : Screen("infos", R.string.infos, Icons.Filled.Info)
    object Profile : Screen("profile", R.string.profile, Icons.Filled.AccountBox)
    object Welcome : Screen("welcome", R.string.welcome, Icons.Filled.AccountBox)
    object Stats : Screen("stats", R.string.stats, Icons.Filled.List)
    object Settings : Screen("settings", R.string.settings, Icons.Filled.Settings)
    object ImageSettings : Screen("imageSettings", R.string.settings, Icons.Filled.Settings)
}