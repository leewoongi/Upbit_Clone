package com.woon.domain.event.service

import com.woon.domain.event.entity.ErrorEvent
import com.woon.domain.event.repository.ErrorEventRepository
import com.woon.domain.event.usecase.SendErrorEventUseCase
import javax.inject.Inject

class SendErrorEventService @Inject constructor(
    private val errorEventRepository: ErrorEventRepository
) : SendErrorEventUseCase {

    override suspend fun invoke(event: ErrorEvent): Result<Unit> {
        return errorEventRepository.sendEvent(event)
    }
}
