package com.example.tglab_task.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tglab_task.R
import com.example.tglab_task.ui.theme.VERTICAL_SPACER_HEIGHT

@Composable
fun ErrorOnContent(
    message: String,
    tryAgainClick: (() -> Unit)? = null
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = message,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 48.dp),
                textAlign = TextAlign.Center
            )
            if(tryAgainClick != null){
                Spacer(modifier = Modifier.height(VERTICAL_SPACER_HEIGHT))
                Button(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                    onClick = tryAgainClick
                ) {
                    Text(
                        text = stringResource(id = R.string.try_again),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 48.dp)
                    )
                }
            }
        }
    }
}