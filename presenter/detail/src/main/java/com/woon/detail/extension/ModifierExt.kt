package com.woon.detail.extension

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.positionChanged

/**
 * 핀치 줌의 좌표 변환 원리
 *
 * 1. 문제상황:
 *    - graphicsLayer는 화면 중심을 기준으로 확대/축소를 수행
 *    - 사용자는 임의의 centroid 지점에서 핀치 줌을 실행
 *    - centroid 위치의 콘텐츠가 줌 후에도 같은 화면 위치에 고정되어야 함
 *
 * 2. 해결방법:
 *    - centroid를 화면 중심 기준 상대좌표로 변환
 *    - 변환된 상대좌표를 기준으로 offset 보정
 *
 * 3. 핵심 공식:
 *    relativeCentroid = centroid - screenCenter
 *    newOffset = oldOffset + (1 - zoomChange) * (relativeCentroid - oldOffset)
 *
 * 4. 물리적 의미:
 *    - 확대로 인해 콘텐츠가 바깥으로 퍼지는 것을
 *    - offset으로 다시 안쪽으로 당겨서 centroid 위치 콘텐츠를 고정
 */
internal fun Modifier.pinchZoom(
    getZoom: () -> Float,
    setZoom: (Float) -> Unit,
    getOffset: () -> Offset,
    setOffset: (Offset) -> Unit,
    minZoom: Float = 1f,
    maxZoom: Float = 2f,
    pinchZoom: ()  -> Unit = {}
): Modifier = this.pointerInput(Unit) {
    awaitEachGesture {
        awaitFirstDown(requireUnconsumed = false)

        do {
            val event = awaitPointerEvent()

            // press 된 포인트가 2개 이상일 때 줌으로 판단
            if (event.changes.count { it.pressed } >= 2) {
                val zoomChange = event.calculateZoom()
                val pan = event.calculatePan()
                val centroid = event.calculateCentroid()

                val oldZoom = getZoom()
                val newZoom = (zoomChange * oldZoom).coerceIn(minZoom, maxZoom)
                val actualZoomChange = if (oldZoom != 0f) newZoom / oldZoom else 1f
                val screenCenter = Offset(size.width / 2f, size.height / 2f)

                val oldOffset = getOffset()
                val newOffset = oldOffset + Offset(
                    x = (1 - actualZoomChange) * (centroid.x - oldOffset.x - screenCenter.x),
                    y = (1 - actualZoomChange) * (centroid.y - oldOffset.y - screenCenter.y)
                ) + pan

                setZoom(newZoom)
                setOffset(newOffset)
                event.changes.forEach { if (it.positionChanged()) it.consume() }
            }

        } while (event.changes.any { it.pressed })
    }
}

/**
 * 드래그
 */
internal fun Modifier.drag(
    getOffset: () -> Offset,
    setOffset: (Offset) -> Unit,
    drag: () -> Unit = {}
): Modifier = this.pointerInput(Unit) {
    awaitEachGesture {
        awaitFirstDown(requireUnconsumed = false)

        do {
            val event = awaitPointerEvent()

            if(event.changes.count { it.pressed } == 1) {
                val change = event.changes.first()
                val dragAmount: Offset = change.positionChange()

                val oldOffset = getOffset()
                val newOffset = oldOffset + dragAmount

                setOffset(newOffset)
                change.consume()
            }
        }while (event.changes.any { it.pressed })
    }
}