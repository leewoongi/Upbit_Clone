package com.woon.domain.event.storage

import com.woon.domain.event.entity.ErrorEvent

interface PendingErrorEventStorage {
    suspend fun save(event: ErrorEvent)
    suspend fun getAll(): List<ErrorEvent>
    suspend fun delete(id: Long)
    suspend fun deleteAll()
    suspend fun count(): Int
}
