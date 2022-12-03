package com.example.tglab_task.ui.games

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tglab_task.R
import com.example.tglab_task.ui.DataCard
import com.example.tglab_task.ui.DataHeader
import com.example.tglab_task.ui.DataText
import com.example.tglab_task.ui.ErrorOnContent
import com.example.tglab_task.ui.theme.HORIZONTAL_CONTENT_PADDING_DP
import com.example.tglab_task.ui.theme.VERTICAL_SPACER_HEIGHT
import timber.log.Timber

data class GameComposeData(
    val homeName: String,
    val homeScore: Int,
    val visitorName: String,
    val visitorScore: Int
)

data class GamesScreenState(
    val teamId: String = "",
    val title: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val games: List<GameComposeData> = emptyList(),
    val currentPage: Int = 0,
    val nextPage: Int? = 1,
)

// Want to use Pager 3.0 but with current BE and after-filtering it's request additional hacking
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun GamesScreen(
    viewModel: GamesViewModel,
    paddingValues: PaddingValues,
    goBackClick: () -> Unit,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
) {
    val listState = rememberLazyListState()
    val viewState by viewModel.state.collectAsState()

    LaunchedEffect(listState.isScrolledToTheEnd()) {
        if (listState.isScrolledToTheEnd() && !viewState.isLoading) {
            Timber.tag("tag_tag").e("scrolled« to bottom")
            viewModel.loadNextPage()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LargeTopAppBar(
            title = {
                Text(
                    text = viewState.title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = goBackClick,
                    content = {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Close")
                    }
                )
            },
            scrollBehavior = topAppBarScrollBehavior
        )

        val errorMessage = viewState.error

        if (viewState.games.isEmpty() && errorMessage != null) {
            ErrorOnContent(message = errorMessage)
        } else {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(
                    start = HORIZONTAL_CONTENT_PADDING_DP,
                    end = HORIZONTAL_CONTENT_PADDING_DP,
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
            ) {
                stickyHeader {
                    Column(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
                        Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT))
                        DataHeader(
                            dataTexts = listOf(
                                "Home Name",
                                "Home Score",
                                "Visitors Name",
                                "Visitors Score",
                            ),
                            maxLines = 2,
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT))
                }
                if(viewState.games.isEmpty() && !viewState.isLoading) {
                    item {
                        ErrorOnContent(message = stringResource(id = R.string.no_games))
                    }
                } else {
                    items(viewState.games) { game ->
                        DataCard(
                            maxLines = 2,
                            dataTexts = listOf(
                                DataText(
                                    text = game.homeName,
                                    center = false
                                ),
                                DataText(
                                    text = game.homeScore.toString(),
                                    center = true
                                ),
                                DataText(
                                    text = game.visitorName,
                                    center = false
                                ),
                                DataText(
                                    text = game.visitorScore.toString(),
                                    center = true
                                ),
                            )
                        )
                        Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT))
                    }
                }
                item {
                    AnimatedVisibility(visible = viewState.isLoading) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = VERTICAL_SPACER_HEIGHT)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(CenterHorizontally).size(64.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun LazyListState.isScrolledToTheEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1