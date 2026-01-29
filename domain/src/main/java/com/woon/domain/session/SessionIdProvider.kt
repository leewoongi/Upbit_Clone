package com.woon.domain.session

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionIdProvider @Inject constructor() {

    private val sessionId: String by lazy {
        UUID.randomUUID().toString()
    }

    fun get(): String = sessionId
}
