package com.example.tglab_task.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class Filter(val title: String) {
    NAME("Name"), CITY("City"), CONFERENCE("Conference")
}


@Composable
fun HomeFilterSwitch(
    selectedFilter: Filter,
    filterSelected: (newFilter: Filter) -> Unit,
    modifier: Modifier = Modifier
) {
    val filterList = listOf(
        Filter.NAME,
        Filter.CITY,
        Filter.CONFERENCE
    )
    val selectedTabIndex = filterList.indexOf(selectedFilter)

    TabRow(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .heightIn(min = 32.dp),
        selectedTabIndex = selectedTabIndex,
        containerColor = MaterialTheme.colorScheme.background,
        indicator = { },
        divider = { }
    ) {
        filterList.forEachIndexed { index, title ->
            val selected = selectedTabIndex == index
            val segmentBackgroundColor by animateColorAsState(
                targetValue = if (selected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surface
                }
            )
            val segmentTextColor by animateColorAsState(
                targetValue = if (selected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .noRippleClickable { filterSelected(filterList[index]) }
                    .clip(RoundedCornerShape(4.dp))
                    .background(segmentBackgroundColor),
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(
                            horizontal = 12.dp, vertical = 8.dp
                        ),
                    text = title.title,
                    fontWeight = if(selected) FontWeight.Bold else null,
                    style = MaterialTheme.typography.bodyMedium.copy(color = segmentTextColor),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SegmentedTabsRowPreview() {
    MaterialTheme {

        var selectedComposeTab by remember {
            mutableStateOf(Filter.CITY)
        }

        Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.background) {
            HomeFilterSwitch(
                selectedFilter = selectedComposeTab,
                filterSelected = { selectedComposeTab = it },
                modifier = Modifier.padding(
                    horizontal = 24.dp, vertical = 12.dp
                )
            )
        }
    }
}

private inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier =
    composed {
        clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            onClick()
        }
    }