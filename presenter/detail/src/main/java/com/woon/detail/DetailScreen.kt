package com.woon.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.woon.detail.ui.SuccessScreen
import com.woon.detail.ui.state.DetailUiState
import com.woon.detail.viewmodel.DetailViewModel

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<DetailViewModel>()
    val uiState = viewModel.uiState.collectAsState().value

    when (uiState) {
        DetailUiState.Loading -> {
            Text("데이터 로딩 중...")
        }
        is DetailUiState.Success -> {
            val candles = uiState.uiModel
            if (candles.isEmpty()) {
                Text("데이터 없음...")
            } else {
                SuccessScreen(
                    modifier = modifier.fillMaxSize(),
                    uiState = uiState,
                    onZoom = {},
                    onDrag = {},
                    onSizeChanged = {  },
                )
            }
        }
    }
}
