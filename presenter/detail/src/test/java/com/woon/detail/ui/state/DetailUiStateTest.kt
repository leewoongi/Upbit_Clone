package com.woon.detail.ui.state

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * DetailUiState 안전성 테스트
 *
 * - Empty candles 상태에서 안전하게 처리되는지 검증
 * - 각 상태 타입이 올바르게 구분되는지 검증
 */
class DetailUiStateTest {

    @Test
    fun `Success with empty candles should be valid`() {
        // Given & When
        val state = DetailUiState.Success(
            marketCode = "KRW-BTC",
            candles = emptyList()
        )

        // Then
        assertTrue(state.candles.isEmpty())
        assertEquals("KRW-BTC", state.marketCode)
    }

    @Test
    fun `Success default candles should be empty list`() {
        // When using default value
        val state = DetailUiState.Success(marketCode = "KRW-BTC")

        // Then
        assertTrue(state.candles.isEmpty())
    }

    @Test
    fun `Loading state should be singleton`() {
        // Given
        val loading1 = DetailUiState.Loading
        val loading2 = DetailUiState.Loading

        // Then
        assertTrue(loading1 === loading2)
    }

    @Test
    fun `Empty state should be singleton`() {
        // Given
        val empty1 = DetailUiState.Empty
        val empty2 = DetailUiState.Empty

        // Then
        assertTrue(empty1 === empty2)
    }

    @Test
    fun `Error state should preserve message`() {
        // Given
        val errorMessage = "네트워크 오류가 발생했습니다"

        // When
        val state = DetailUiState.Error(errorMessage)

        // Then
        assertEquals(errorMessage, state.message)
    }

    @Test
    fun `state types should be distinguishable`() {
        // Given
        val loading: DetailUiState = DetailUiState.Loading
        val success: DetailUiState = DetailUiState.Success("KRW-BTC")
        val error: DetailUiState = DetailUiState.Error("error")
        val empty: DetailUiState = DetailUiState.Empty

        // Then - when expressions should work correctly
        when (loading) {
            is DetailUiState.Loading -> assertTrue(true)
            else -> assertTrue(false)
        }

        when (success) {
            is DetailUiState.Success -> assertTrue(true)
            else -> assertTrue(false)
        }

        when (error) {
            is DetailUiState.Error -> assertTrue(true)
            else -> assertTrue(false)
        }

        when (empty) {
            is DetailUiState.Empty -> assertTrue(true)
            else -> assertTrue(false)
        }
    }
}
