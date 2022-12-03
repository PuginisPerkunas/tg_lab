package com.example.tglab_task

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.tglab_task.models.PlayerData
import com.example.tglab_task.ui.games.GamesScreen
import com.example.tglab_task.ui.games.GamesViewModel
import com.example.tglab_task.ui.home.HomeScreen
import com.example.tglab_task.ui.home.HomeViewModel
import com.example.tglab_task.ui.players.PlayerInfoScreen
import com.example.tglab_task.ui.players.PlayersScreen
import com.example.tglab_task.ui.players.PlayersViewModel
import com.example.tglab_task.ui.players.SharedPlayersViewModel

val bottomNavItems = listOf(
    ScreenNavigation.HomeNav,
    ScreenNavigation.PlayersNav
)

sealed class ScreenNavigation(
    val route: String,
    @StringRes val title: Int,
    val icon: ImageVector
) {
    object HomeNav : ScreenNavigation(
        route = HomeNavigation.prefix,
        title = R.string.nav_title_home,
        icon = Icons.Default.Home
    )

    object PlayersNav : ScreenNavigation(
        route = PlayersNavigation.prefix,
        title = R.string.nav_title_players,
        icon = Icons.Default.Person
    )
}

// Todo where to put bottom sheet screen
sealed class HomeNavigation(val route: String) {
    companion object {
        const val prefix = "home"
        const val TEAM_ID = "team_id"
        const val TEAM_NAME = "team_name"
    }

    object HomeStartNav : HomeNavigation("$prefix/home_start")
    object TeamGamesNav : HomeNavigation("$prefix/team_games/{$TEAM_ID}/{$TEAM_NAME}") {
        fun createRoute(teamId: String, teamName: String) = "$prefix/team_games/$teamId/$teamName"
    }
}

sealed class PlayersNavigation(val route: String) {
    companion object {
        const val prefix = "players"
        const val PLAYER_INFO = "player_info"
    }

    object PlayersStartNav : PlayersNavigation("$prefix/players_start")
    object PlayerInfoNav : PlayersNavigation("$prefix/players_info/")
}

data class TeamGamesArguments(
    val id: String = "",
    val name: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.HomeNavigation(
    navController: NavController,
    paddingValues: PaddingValues,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
) {
    val stateOfTeamsArguments = mutableStateOf(TeamGamesArguments())

    navigation(
        route = ScreenNavigation.HomeNav.route,
        startDestination = HomeNavigation.HomeStartNav.route
    ) {
        composable(
            route = HomeNavigation.HomeStartNav.route
        ) {
            val viewModel: HomeViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()
            HomeScreen(
                homeState = state,
                tryAgainClick = { viewModel.tryAgainClick() },
                teamClick = { teamId, teamName ->
                    navController.navigate(
                        HomeNavigation.TeamGamesNav.createRoute(
                            teamId = teamId,
                            teamName = teamName
                        )
                    )
                },
                topAppBarScrollBehavior = topAppBarScrollBehavior,
                filterSelected = {
                    viewModel.newFilterSelected(it)
                },
            )
        }
        composable(
            route = HomeNavigation.TeamGamesNav.route,
            arguments = listOf(
                navArgument(HomeNavigation.TEAM_ID) { type = NavType.StringType },
                navArgument(HomeNavigation.TEAM_NAME) { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val viewModel: GamesViewModel = hiltViewModel()
            val id = backStackEntry.arguments?.getString(HomeNavigation.TEAM_ID)
                ?: throw Throwable("must not be null")
            val name = backStackEntry.arguments?.getString(HomeNavigation.TEAM_NAME)
                ?: throw Throwable("must not be null")

            stateOfTeamsArguments.value = TeamGamesArguments(
                id = id,
                name = name
            )

            LaunchedEffect(stateOfTeamsArguments) {
                if (stateOfTeamsArguments.value.id.isNotEmpty()) {
                    viewModel.setTeamInfo(
                        teamId = stateOfTeamsArguments.value.id,
                        teamName = stateOfTeamsArguments.value.name
                    )
                }
            }
            GamesScreen(
                // Passing view model is a bad behavior (how preview should be wrote??)
                // Keeping as example
                viewModel = viewModel,
                paddingValues = paddingValues,
                topAppBarScrollBehavior = topAppBarScrollBehavior,
                goBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.PlayersNavigation(
    sharedPlayersViewModel: SharedPlayersViewModel,
    navController: NavController,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
) {
    navigation(
        route = ScreenNavigation.PlayersNav.route,
        startDestination = PlayersNavigation.PlayersStartNav.route
    ) {
        composable(
            route = PlayersNavigation.PlayersStartNav.route
        ) {
            val viewModel = hiltViewModel<PlayersViewModel>()
            PlayersScreen(
                searchText = viewModel.textSearch.value,
                playersStream = viewModel.searchedPlayers,
                searchTextChanges = { viewModel.setPlayerQuery(it) },
                clearSearchTextClick = { viewModel.clearText() },
                tryAgainClick = { viewModel.tryAgainClick() },
                topAppBarScrollBehavior = topAppBarScrollBehavior,
                onPlayerClick = {
                    sharedPlayersViewModel.setPlayer(it)
                    navController.navigate(PlayersNavigation.PlayerInfoNav.route)
                }
            )
        }
        composable(
            route = PlayersNavigation.PlayerInfoNav.route
        ){
            val playerData = sharedPlayersViewModel.selectedPlayerData
            if(playerData != null) {
                PlayerInfoScreen(
                    playerData = playerData,
                    topAppBarScrollBehavior = topAppBarScrollBehavior,
                    backClick = {
                        navController.popBackStack()
                    }
                )
            } else {
                throw Throwable("must not be null")
            }
        }
    }
}
