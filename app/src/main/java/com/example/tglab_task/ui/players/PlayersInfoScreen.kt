package com.example.tglab_task.ui.players

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.tglab_task.R
import com.example.tglab_task.models.PlayerData
import com.example.tglab_task.ui.theme.HORIZONTAL_CONTENT_PADDING_DP
import com.example.tglab_task.ui.theme.VERTICAL_SPACER_HEIGHT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerInfoScreen(
    backClick: () -> Unit,
    playerData: PlayerData,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LargeTopAppBar(
            title = {
                Column(Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(
                            id = R.string.name_surname,
                            playerData.first_name,
                            playerData.last_name
                        ),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = backClick,
                    content = {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Close")
                    }
                )
            },
            scrollBehavior = topAppBarScrollBehavior
        )
        Column(modifier = Modifier.padding(horizontal = HORIZONTAL_CONTENT_PADDING_DP)) {
            Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT))
            with(playerData){
                InfoTextRow(label = stringResource(id = R.string.player_name), value = first_name)
                Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT / 2))
                InfoTextRow(label = stringResource(id = R.string.player_surname), value = last_name)
                Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT / 2))
                InfoTextRow(label = stringResource(id = R.string.player_team_full_name), value = team.full_name)
                Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT / 2))
                InfoTextRow(label = stringResource(id = R.string.position), value = position)
                Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT / 2))
                height_feet?.let {
                    InfoTextRow(
                        label = stringResource(id = R.string.height_feet),
                        value = stringResource(id = R.string.height_feet_value, String.format("%.2f", it))
                    )
                    Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT / 2))
                }
                height_inches?.let {
                    InfoTextRow(
                        label = stringResource(id = R.string.height_inches),
                        value = stringResource(id = R.string.inch_value, String.format("%.2f", it))
                    )
                    Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT / 2))
                }
                weight_pounds?.let {
                    InfoTextRow(
                        label = stringResource(id = R.string.weight_pounds),
                        value = stringResource(id = R.string.weight_pounds_value, String.format("%.2f", it))
                    )
                    Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT / 2))
                }
            }
            Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT))
        }
    }
}

@Composable
private fun InfoTextRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(0.4f),
                text = label,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.width(HORIZONTAL_CONTENT_PADDING_DP))
            Text(
                modifier = Modifier.weight(1f),
                text = value,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
    }
}