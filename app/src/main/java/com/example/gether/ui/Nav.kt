package com.example.gether.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Routes { const val Feed = "feed"; const val ComposePost = "compose"; const val Profile = "profile" }

@Composable
fun AppNav(start: String = Routes.Feed) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = start) {
        composable(Routes.Feed) { FeedScreen(onCompose = { nav.navigate(Routes.ComposePost) }, onProfile = { nav.navigate(Routes.Profile) }) }
        composable(Routes.ComposePost) { ComposePostScreen(onDone = { nav.popBackStack() }) }
        composable(Routes.Profile) { ProfileScreen(onBack = { nav.popBackStack() }) }
    }
}
