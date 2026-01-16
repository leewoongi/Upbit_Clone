package com.woon.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.woon.core.ui.design.component.edittext.ChartSearchBar

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit
){
    ChartSearchBar(
        modifier = modifier,
        query = query,
        onQueryChange = { onQueryChange(it) },
        onSearch = { onSearch(it) }
    )
}