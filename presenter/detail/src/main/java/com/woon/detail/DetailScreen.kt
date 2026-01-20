package com.woon.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.woon.detail.viewmodel.DetailViewModel

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    marketCode: String
) {
    val viewModel = hiltViewModel<DetailViewModel>()
}
