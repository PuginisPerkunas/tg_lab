package com.example.tglab_task.ui.players

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.example.tglab_task.R
import com.example.tglab_task.ui.SearchBar
import com.example.tglab_task.models.PlayerData
import com.example.tglab_task.ui.DataCard
import com.example.tglab_task.ui.DataHeader
import com.example.tglab_task.ui.DataText
import com.example.tglab_task.ui.ErrorOnContent
import com.example.tglab_task.ui.LoadingOnContent
import com.example.tglab_task.ui.theme.HORIZONTAL_CONTENT_PADDING_DP
import com.example.tglab_task.ui.theme.VERTICAL_SPACER_HEIGHT
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PlayersScreen(
    searchText: String,
    playersStream: MutableStateFlow<PagingData<PlayerData>>,
    searchTextChanges: (newText: String) -> Unit,
    clearSearchTextClick: () -> Unit,
    tryAgainClick: () -> Unit,
    onPlayerClick: (player: PlayerData) -> Unit,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
) {
    val players = playersStream.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize()) {
        LargeTopAppBar(
            title = {
                Column(Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(id = R.string.title_players),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

            },
            scrollBehavior = topAppBarScrollBehavior
        )
        // Wanted to use StickyHeader but there is a bug while using Pager3
        // https://issuetracker.google.com/issues/177245496
        // So using bypass
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = HORIZONTAL_CONTENT_PADDING_DP)) {
            Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT))
            ContentHeader(
                text = searchText,
                textValueChanged = { searchTextChanges(it) },
                clearTextClick = clearSearchTextClick
            )
        }
        val pagerState = players.loadState.append
        LazyColumn(
            contentPadding = PaddingValues(horizontal = HORIZONTAL_CONTENT_PADDING_DP)
        ) {
            itemsIndexed(players, key = { _, item -> item.id }) { index, player ->
                player?.let {
                    if(index == 0) {
                        Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT))
                    }
                    DataCard(
                        dataTexts = listOf(
                            DataText(player.first_name),
                            DataText(player.last_name),
                            DataText(player.team.name),
                        ),
                        onClick = {
                            onPlayerClick(player)
                        },
                        modifier = Modifier.animateItemPlacement()
                    )
                    Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT))
                }
            }
            when(pagerState){
                is LoadState.Error -> item {
                    ErrorOnContent(
                        message = pagerState.error.message
                            ?: stringResource(id = R.string.generic_error),
                        tryAgainClick = tryAgainClick
                    )
                }
                LoadState.Loading -> item {
                    LoadingOnContent()
                }
                else -> Unit
            }
        }
    }
}

@Composable
private fun ContentHeader(
    text: String,
    textValueChanged: (String) -> Unit,
    clearTextClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(Modifier.fillMaxWidth()) {
            SearchBar(
                text = text,
                onValueChanged = textValueChanged,
                onClear = clearTextClick
            )
            Spacer(modifier = Modifier.height(12.dp))
            DataHeader(dataTexts = listOf(
                stringResource(id = R.string.first_name),
                stringResource(id = R.string.last_name),
                stringResource(id = R.string.team),
            ))
        }
    }
}