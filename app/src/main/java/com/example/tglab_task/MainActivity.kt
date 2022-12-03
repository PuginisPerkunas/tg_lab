package com.example.tglab_task

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tglab_task.ui.players.SharedPlayersViewModel
import com.example.tglab_task.ui.theme.TGLabtaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TGLabEntryScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TGLabEntryScreen() {
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    TGLabtaskTheme {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            content = { paddingValues ->
                MainContent(
                    navController = navController,
                    paddingValues = paddingValues,
                    topAppBarScrollBehavior = scrollBehavior
                )
            },
            bottomBar = {
                BottomBar(
                    navController = navController
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    navController: NavHostController,
    paddingValues: PaddingValues,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
) {
    val sharedPlayersViewModel = hiltViewModel<SharedPlayersViewModel>()
    NavHost(
        navController = navController,
        startDestination = ScreenNavigation.HomeNav.route,
        modifier = Modifier.padding(
            top = paddingValues.calculateTopPadding(),
            bottom = paddingValues.calculateBottomPadding()
        )
    ) {
        HomeNavigation(
            navController = navController,
            paddingValues = paddingValues,
            topAppBarScrollBehavior = topAppBarScrollBehavior
        )

        PlayersNavigation(
            sharedPlayersViewModel = sharedPlayersViewModel,
            navController = navController,
            topAppBarScrollBehavior = topAppBarScrollBehavior
        )
    }
}

@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar {
        bottomNavItems.forEach { screen ->
            val title = stringResource(id = screen.title)
            NavigationBarItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = title) },
                label = { Text(text = title) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}