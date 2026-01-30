package com.woon.repository.event

import com.woon.datasource.local.room.dao.PendingErrorEventDao
import com.woon.domain.event.entity.ErrorEvent
import com.woon.domain.event.storage.PendingErrorEventStorage
import com.woon.repository.event.local.mapper.toDomain
import com.woon.repository.event.local.mapper.toEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PendingErrorEventStorageImpl @Inject constructor(
    private val dao: PendingErrorEventDao
) : PendingErrorEventStorage {

    override suspend fun save(event: ErrorEvent) {
        // 최대 10개까지만 저장
        val currentCount = dao.count()
        if (currentCount >= MAX_PENDING_EVENTS) {
            // 가장 오래된 것 삭제
            val oldest = dao.getOldest(1).firstOrNull()
            oldest?.let { dao.deleteById(it.id) }
        }
        dao.insert(event.toEntity())
    }

    override suspend fun getAll(): List<ErrorEvent> {
        return dao.getAll().map { it.toDomain() }
    }

    override suspend fun delete(id: Long) {
        dao.deleteById(id)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun count(): Int {
        return dao.count()
    }

    companion object {
        private const val MAX_PENDING_EVENTS = 10
    }
}
