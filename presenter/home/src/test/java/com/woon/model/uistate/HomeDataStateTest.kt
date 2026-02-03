package com.woon.model.uistate

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * HomeDataState 안전성 테스트
 *
 * - Empty coins 상태에서 안전하게 처리되는지 검증
 * - 각 상태 타입이 올바르게 구분되는지 검증
 */
class HomeDataStateTest {

    @Test
    fun `Success with empty coins should be valid`() {
        // Given & When
        val state = HomeDataState.Success(coins = emptyList())

        // Then
        assertTrue(state.coins.isEmpty())
    }

    @Test
    fun `Loading state should be singleton`() {
        // Given
        val loading1 = HomeDataState.Loading
        val loading2 = HomeDataState.Loading

        // Then
        assertTrue(loading1 === loading2)
    }

    @Test
    fun `Empty state should be singleton`() {
        // Given
        val empty1 = HomeDataState.Empty
        val empty2 = HomeDataState.Empty

        // Then
        assertTrue(empty1 === empty2)
    }

    @Test
    fun `Error state should preserve message`() {
        // Given
        val errorMessage = "네트워크 오류가 발생했습니다"

        // When
        val state = HomeDataState.Error(errorMessage)

        // Then
        assertEquals(errorMessage, state.message)
    }

    @Test
    fun `state types should be distinguishable with when expression`() {
        // Given
        val loading: HomeDataState = HomeDataState.Loading
        val success: HomeDataState = HomeDataState.Success(emptyList())
        val error: HomeDataState = HomeDataState.Error("error")
        val empty: HomeDataState = HomeDataState.Empty

        // Then - when expressions should work correctly (simulating UI rendering)
        val loadingResult = when (loading) {
            is HomeDataState.Loading -> "loading"
            is HomeDataState.Success -> "success"
            is HomeDataState.Error -> "error"
            is HomeDataState.Empty -> "empty"
        }
        assertEquals("loading", loadingResult)

        val successResult = when (success) {
            is HomeDataState.Loading -> "loading"
            is HomeDataState.Success -> "success"
            is HomeDataState.Error -> "error"
            is HomeDataState.Empty -> "empty"
        }
        assertEquals("success", successResult)

        val errorResult = when (error) {
            is HomeDataState.Loading -> "loading"
            is HomeDataState.Success -> "success"
            is HomeDataState.Error -> "error"
            is HomeDataState.Empty -> "empty"
        }
        assertEquals("error", errorResult)

        val emptyResult = when (empty) {
            is HomeDataState.Loading -> "loading"
            is HomeDataState.Success -> "success"
            is HomeDataState.Error -> "error"
            is HomeDataState.Empty -> "empty"
        }
        assertEquals("empty", emptyResult)
    }
}
