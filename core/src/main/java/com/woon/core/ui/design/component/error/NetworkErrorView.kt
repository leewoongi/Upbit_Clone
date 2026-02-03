package com.woon.core.ui.design.component.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.woon.core.network.NetworkError
import com.woon.core.network.NetworkUiState
import com.woon.core.ui.design.theme.color.colorError
import com.woon.core.ui.design.theme.color.colorOnSurface
import com.woon.core.ui.design.theme.color.colorPrimary
import com.woon.core.ui.design.theme.color.colorSurfaceVariant

/**
 * 네트워크 에러 표시 뷰
 *
 * 에러 유형에 따른 아이콘, 메시지, 재시도 버튼을 표시한다.
 *
 * @param error 네트워크 에러
 * @param onRetry 재시도 버튼 클릭 콜백
 * @param modifier Modifier
 * @param isAutoRetrying 자동 재시도 중 여부
 * @param retryAttempt 현재 재시도 횟수
 */
@Composable
fun NetworkErrorView(
    error: NetworkError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    isAutoRetrying: Boolean = false,
    retryAttempt: Int = 0
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            // 에러 아이콘 (타입별 차별화)
            ErrorIcon(error.type)

            Spacer(modifier = Modifier.height(16.dp))

            // 에러 메시지
            Text(
                text = error.userMessage,
                fontSize = 16.sp,
                color = colorOnSurface,
                textAlign = TextAlign.Center
            )

            // 자동 재시도 중이면 진행 상태 표시
            if (isAutoRetrying) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = colorPrimary,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "재시도 중... (${retryAttempt}회)",
                    fontSize = 12.sp,
                    color = colorSurfaceVariant
                )
            }

            // 재시도 버튼 (자동 재시도 중이 아닐 때만)
            if (!isAutoRetrying && error.isRetryable) {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorPrimary
                    )
                ) {
                    Text(
                        text = error.retryButtonText,
                        color = Color.White
                    )
                }
            }

            // 재시도 불가능한 에러는 안내 텍스트
            if (!error.isRetryable) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "문제가 지속되면 앱을 다시 시작해주세요",
                    fontSize = 12.sp,
                    color = colorSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * 에러 타입별 아이콘
 */
@Composable
private fun ErrorIcon(type: NetworkError.Type) {
    val iconTint = when (type) {
        NetworkError.Type.RATE_LIMIT -> Color(0xFFFF9800)  // Orange
        NetworkError.Type.SSL -> Color(0xFFF44336)         // Red
        NetworkError.Type.SERVER -> Color(0xFFE91E63)      // Pink
        else -> colorError
    }

    // TODO: 실제 아이콘 리소스로 교체
    // 현재는 placeholder로 텍스트 사용
    Box(
        modifier = Modifier
            .size(64.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (type) {
                NetworkError.Type.TIMEOUT -> "⏱"
                NetworkError.Type.IO, NetworkError.Type.DNS -> "📶"
                NetworkError.Type.SSL -> "🔒"
                NetworkError.Type.RATE_LIMIT -> "⚠️"
                NetworkError.Type.SERVER -> "🖥"
                NetworkError.Type.WEBSOCKET -> "🔌"
                else -> "❌"
            },
            fontSize = 48.sp
        )
    }
}

/**
 * NetworkUiState.Error를 위한 편의 함수
 */
@Composable
fun NetworkErrorView(
    errorState: NetworkUiState.Error,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    NetworkErrorView(
        error = errorState.error,
        onRetry = onRetry,
        modifier = modifier,
        isAutoRetrying = errorState.isAutoRetrying,
        retryAttempt = errorState.retryState.attemptCount
    )
}

/**
 * 인라인 에러 표시 (토스트/스낵바용 간단 메시지)
 */
@Composable
fun InlineNetworkError(
    error: NetworkError,
    modifier: Modifier = Modifier
) {
    Text(
        text = error.userMessage,
        color = colorError,
        fontSize = 14.sp,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
