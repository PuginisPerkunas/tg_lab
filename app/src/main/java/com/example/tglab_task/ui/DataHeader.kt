package com.example.tglab_task.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tglab_task.ui.theme.DATA_HEADER_CONTENT_PADDING

@Composable
fun DataHeader(
    dataTexts: List<String>,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    includeNavigatingSpacer: Boolean = true,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(DATA_HEADER_CONTENT_PADDING),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val weight = (1 % dataTexts.size).toFloat()
            dataTexts.forEachIndexed { index, dataText ->
                Text(
                    text = dataText,
                    modifier = Modifier.weight(weight),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis,
                )
                if (index != dataText.lastIndex) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            if(includeNavigatingSpacer){
                Spacer(modifier = Modifier.width(24.dp))
            }
        }
        Divider()
    }
}

@Preview(showBackground = true)
@Composable
private fun DataHeaderPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                DataHeader(dataTexts = listOf("Team", "City", "Conference"))
                Spacer(modifier = Modifier.height(24.dp))
                DataHeader(dataTexts = listOf("Team", "City", "Conference", "Other", "Data"))
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}