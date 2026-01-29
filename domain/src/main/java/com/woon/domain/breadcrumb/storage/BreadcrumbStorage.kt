package com.woon.domain.breadcrumb.storage

import com.woon.domain.breadcrumb.model.Breadcrumb

interface BreadcrumbStorage {
    fun saveSnapshot(breadcrumbs: List<Breadcrumb>)
    fun loadSnapshot(): Pair<List<Breadcrumb>, Long>?
    fun clearSnapshot()
}
