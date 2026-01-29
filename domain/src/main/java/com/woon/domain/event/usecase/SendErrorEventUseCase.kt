package com.woon.domain.event.usecase

import com.woon.domain.event.entity.ErrorEvent

interface SendErrorEventUseCase {
    suspend operator fun invoke(event: ErrorEvent): Result<Unit>
}
