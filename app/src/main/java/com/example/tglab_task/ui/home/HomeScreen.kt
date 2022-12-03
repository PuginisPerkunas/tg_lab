package com.example.tglab_task.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tglab_task.R
import com.example.tglab_task.ui.DataCard
import com.example.tglab_task.ui.DataHeader
import com.example.tglab_task.ui.ErrorOnContent
import com.example.tglab_task.ui.Filter
import com.example.tglab_task.ui.HomeFilterSwitch
import com.example.tglab_task.ui.LoadingOnContent
import com.example.tglab_task.ui.theme.HORIZONTAL_CONTENT_PADDING_DP
import com.example.tglab_task.ui.theme.VERTICAL_SPACER_HEIGHT

data class TeamHomeComposeData(
    val teamId: String,
    val teamName: String,
    val teamCity: String,
    val teamConference: String
)

data class HomeState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val teams: List<TeamHomeComposeData> = emptyList(),
    val selectedFilter: Filter = Filter.NAME
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeState: HomeState,
    tryAgainClick: () -> Unit,
    teamClick: (teamId: String, teamName: String) -> Unit,
    filterSelected: (newFilter: Filter) -> Unit,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LargeTopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.title_home),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            scrollBehavior = topAppBarScrollBehavior
        )
        when {
            homeState.isLoading -> LoadingOnContent()
            homeState.errorMessage != null -> {
                ErrorOnContent(
                    message = homeState.errorMessage,
                    tryAgainClick = tryAgainClick
                )
            }
            homeState.teams.isEmpty() -> ErrorOnContent(
                message = stringResource(id = R.string.generic_error),
                tryAgainClick = tryAgainClick
            )
            else -> ScreenContent(
                teams = homeState.teams,
                teamClick = teamClick,
                selectedFilter = homeState.selectedFilter,
                filterSelected = filterSelected
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScreenContent(
    teams: List<TeamHomeComposeData>,
    teamClick: (teamId: String, teamName: String) -> Unit,
    selectedFilter: Filter,
    filterSelected: (newFilter: Filter) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = HORIZONTAL_CONTENT_PADDING_DP)
    ) {
        stickyHeader {
            Column(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
                Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT))
                HomeFilterSwitch(selectedFilter = selectedFilter, filterSelected = filterSelected)
                Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT))

            }
        }
        item {
            Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT))
        }
        items(teams, key = { it.teamId }) { team ->
            val dataTextMapped = listOf(
                team.teamName,
                team.teamCity,
                team.teamConference
            )
            DataCard(
                dataTexts = dataTextMapped,
                onClick = {
                    teamClick(team.teamId, team.teamName)
                },
                modifier = Modifier.animateItemPlacement()
            )
            Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT))
        }
        item {
            Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun HomeLoadingPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            HomeScreen(
                homeState = HomeState(),
                tryAgainClick = { },
                teamClick = { _, _ ->  },
                topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
                filterSelected = {},
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun HomeEmptyPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            HomeScreen(
                homeState = HomeState(
                    isLoading = false,
                    teams = emptyList()
                ),
                tryAgainClick = { },
                teamClick = { _, _ ->  },
                topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
                filterSelected = {},
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            HomeScreen(
                homeState = HomeState(
                    isLoading = false,
                    teams = listOf(
                        TeamHomeComposeData(
                            teamId = "1",
                            teamName = "Atlanta Hawks",
                            teamCity = "Atlanta",
                            teamConference = "East"
                        ),
                        TeamHomeComposeData(
                            teamId = "2",
                            teamName = "Vilniaus Rytas",
                            teamCity = "Vilnius",
                            teamConference = "EU"
                        ),
                        TeamHomeComposeData(
                            teamId = "3",
                            teamName = "Vilkai",
                            teamCity = "Alytus",
                            teamConference = "Dzukija"
                        ),
                    )
                ),
                tryAgainClick = { },
                teamClick = { _, _ ->  },
                topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
                filterSelected = {},
            )
        }
    }
}