package com.woon.core.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SignalWifiOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.woon.core.network.NetworkError

/**
 * 네트워크 에러 표시 컴포넌트
 *
 * 에러 유형에 따른 아이콘, 메시지, 재시도 버튼을 표시한다.
 *
 * @param error 네트워크 에러 정보
 * @param onRetry 재시도 버튼 클릭 콜백
 * @param modifier Modifier
 * @param canRetry 재시도 가능 여부
 * @param isAutoRetrying 자동 재시도 진행 중 여부
 * @param retryCount 현재까지 재시도 횟수
 */
@Composable
fun NetworkErrorView(
    error: NetworkError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    canRetry: Boolean = error.isRetryable,
    isAutoRetrying: Boolean = false,
    retryCount: Int = 0
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            // 에러 아이콘
            Icon(
                imageVector = getErrorIcon(error.type),
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = getErrorIconTint(error.type)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 에러 메시지
            Text(
                text = error.userMessage,
                fontSize = 16.sp,
                color = Color(0xFFB0B0B0),
                textAlign = TextAlign.Center
            )

            // 재시도 횟수 표시 (자동 재시도 중일 때)
            AnimatedVisibility(
                visible = isAutoRetrying,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFF3B82F6),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "재시도 중... ($retryCount/2)",
                        fontSize = 12.sp,
                        color = Color(0xFF808080)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 재시도 버튼
            if (canRetry && !isAutoRetrying) {
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B82F6)
                    )
                ) {
                    Text(
                        text = error.retryButtonText,
                        color = Color.White
                    )
                }
            }

            // 자동 재시도 소진 안내
            if (!canRetry && retryCount > 0) {
                Text(
                    text = "자동 재시도가 실패했습니다.\n잠시 후 다시 시도해주세요.",
                    fontSize = 12.sp,
                    color = Color(0xFF808080),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

/**
 * 에러 유형별 아이콘
 */
private fun getErrorIcon(type: NetworkError.Type): ImageVector {
    return when (type) {
        NetworkError.Type.TIMEOUT -> Icons.Filled.HourglassEmpty
        NetworkError.Type.IO -> Icons.Filled.SignalWifiOff
        NetworkError.Type.DNS -> Icons.Filled.CloudOff
        NetworkError.Type.SSL -> Icons.Filled.Lock
        NetworkError.Type.RATE_LIMIT -> Icons.Filled.HourglassEmpty
        NetworkError.Type.SERVER -> Icons.Filled.Cloud
        NetworkError.Type.CLIENT -> Icons.Filled.Error
        NetworkError.Type.PARSE -> Icons.Filled.Warning
        NetworkError.Type.WEBSOCKET -> Icons.Filled.SignalWifiOff
        NetworkError.Type.UNKNOWN -> Icons.Filled.Error
    }
}

/**
 * 에러 유형별 아이콘 색상
 */
private fun getErrorIconTint(type: NetworkError.Type): Color {
    return when (type) {
        NetworkError.Type.TIMEOUT,
        NetworkError.Type.IO,
        NetworkError.Type.DNS -> Color(0xFFFFA500)  // Orange - retryable
        NetworkError.Type.SSL -> Color(0xFFEF4444)  // Red - security
        NetworkError.Type.RATE_LIMIT -> Color(0xFFFFA500)  // Orange - rate limit
        NetworkError.Type.SERVER -> Color(0xFFEF4444)  // Red - server error
        NetworkError.Type.CLIENT -> Color(0xFFEF4444)  // Red - client error
        NetworkError.Type.PARSE -> Color(0xFF808080)  // Gray - parse error
        NetworkError.Type.WEBSOCKET -> Color(0xFFFFA500)  // Orange - ws error
        NetworkError.Type.UNKNOWN -> Color(0xFF808080)  // Gray - unknown
    }
}

/**
 * 간단한 에러 메시지와 재시도 버튼
 */
@Composable
fun SimpleErrorView(
    message: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = message,
                fontSize = 16.sp,
                color = Color(0xFFB0B0B0),
                textAlign = TextAlign.Center
            )

            if (onRetry != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B82F6)
                    )
                ) {
                    Text(
                        text = "재시도",
                        color = Color.White
                    )
                }
            }
        }
    }
}
