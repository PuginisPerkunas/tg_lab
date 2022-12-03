package com.example.tglab_task.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tglab_task.ui.theme.DATA_CARD_CONTENT_PADDING
import com.example.tglab_task.ui.theme.DATA_CARD_ELEVATION
import com.example.tglab_task.ui.theme.HORIZONTAL_CONTENT_PADDING_DP
import com.example.tglab_task.ui.theme.VERTICAL_SPACER_HEIGHT

data class DataText(
    val text: String,
    val center: Boolean = false
)
@Composable
fun DataCardV2(
    dataTexts: List<DataText>,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier.run {
            if(onClick != null) {
                this.clip(CardDefaults.shape).clickable { onClick() }
            } else {
                this
            }
        },
        elevation = CardDefaults.cardElevation(defaultElevation = DATA_CARD_ELEVATION)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DATA_CARD_CONTENT_PADDING),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val weight = (1 % dataTexts.size).toFloat()
            dataTexts.forEachIndexed { index, dataText ->
                Text(
                    text = dataText.text,
                    modifier = Modifier.weight(weight),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = if(dataText.center) TextAlign.Center else null
                )
                if (index != dataTexts.lastIndex) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            if(onClick != null){
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null
                )
            } else {
                Spacer(modifier = Modifier.width(24.dp))
            }
        }
    }
}

@Composable
fun DataCard(
    dataTexts: List<String>,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier.run {
            if(onClick != null) {
                this.clip(CardDefaults.shape).clickable { onClick() }
            } else {
                this
            }
        },
        elevation = CardDefaults.cardElevation(defaultElevation = DATA_CARD_ELEVATION)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DATA_CARD_CONTENT_PADDING),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val weight = (1 % dataTexts.size).toFloat()
            dataTexts.forEachIndexed { index, dataText ->
                Text(
                    text = dataText,
                    modifier = Modifier.weight(weight),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis,
                )
                if (index != dataText.lastIndex) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            if(onClick != null){
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null
                )
            } else {
                Spacer(modifier = Modifier.width(24.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DataCardPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(horizontal = HORIZONTAL_CONTENT_PADDING_DP)) {
                DataCard(
                    dataTexts = listOf(
                        "TeamName", "TeamCity", "Conference"
                    ),
                )
                Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT))
                DataCard(
                    dataTexts = listOf(
                        "Second", "Secc", "East"
                    ),
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT))
                DataCard(
                    dataTexts = listOf(
                        "Second", "Secc", "East", "Secc", "East"
                    ),
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT))
                DataCard(
                    dataTexts = listOf(
                        "Second", "Secc", "East", "Second", "Secc", "East",
                    ),
                    onClick = {}
                )
            }
        }
    }
}