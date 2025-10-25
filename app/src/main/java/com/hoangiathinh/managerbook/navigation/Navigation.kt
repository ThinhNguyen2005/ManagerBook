package com.hoangiathinh.managerbook.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hoangiathinh.managerbook.model.MainViewModel
import com.hoangiathinh.managerbook.ui.screen.HomeScreen

sealed class Screen(val route: String){
    object Home: Screen("home")
    object ListBook: Screen("list_book")
    object Accout: Screen("accout")

}

@Composable
fun AppNavHost(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = mainViewModel,
            )
        }
        composable(Screen.ListBook.route) {
        }
        composable(Screen.Accout.route) {
        }
    }
}
@Composable
fun ManagerBottomBar(
    navController: NavHostController,
    currentRoute: String?
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == Screen.Home.route,
            onClick = { navController.navigate(Screen.Home.route) },
            icon = { Icon(Icons.Filled.Home, null) },
            label = { Text("Quản lý") }
        )
        NavigationBarItem(
            selected = currentRoute == Screen.ListBook.route,
            onClick = { navController.navigate(Screen.ListBook.route) },
            icon = { Icon(Icons.Filled.List, null) },
            label = { Text("DS Sách") }
        )
        NavigationBarItem(
            selected = currentRoute == Screen.Accout.route,
            onClick = { navController.navigate(Screen.Accout.route) },
            icon = { Icon(Icons.Filled.Person, null) },
            label = { Text("Sinh viên") },
        )
    }
}