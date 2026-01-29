package com.woon.repository.event

import com.woon.datasource.remote.http.event.ErrorEventDataSource
import com.woon.domain.event.entity.ErrorEvent
import com.woon.domain.event.repository.ErrorEventRepository
import com.woon.repository.event.mapper.toRequest
import javax.inject.Inject

class ErrorEventRepositoryImpl @Inject constructor(
    private val errorEventDataSource: ErrorEventDataSource
) : ErrorEventRepository {

    override suspend fun sendEvent(event: ErrorEvent): Result<Unit> {
        return runCatching {
            errorEventDataSource.sendEvent(event.toRequest())
        }
    }
}
