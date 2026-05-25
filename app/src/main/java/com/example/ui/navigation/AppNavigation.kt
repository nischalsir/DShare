package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.FileShareViewModel
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.receive.ReceiveScreen
import com.example.ui.screens.send.SendScreen
import com.example.ui.screens.transfer.TransferScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val fileShareViewModel: FileShareViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") {
            HomeScreen(
                onNavigateToSend = { navController.navigate("send") },
                onNavigateToReceive = { navController.navigate("receive") }
            )
        }
        composable("send") {
            SendScreen(
                viewModel = fileShareViewModel,
                onBack = { navController.popBackStack() },
                onTransferStarted = { navController.navigate("transfer") }
            )
        }
        composable("receive") {
            ReceiveScreen(
                viewModel = fileShareViewModel,
                onBack = { navController.popBackStack() },
                onTransferStarted = { navController.navigate("transfer") }
            )
        }
        composable("transfer") {
            TransferScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
