package com.ramon.ramondrive.domain.usecase

import com.ramon.ramondrive.core.common.AppResult
import com.ramon.ramondrive.domain.repository.TransferRepository
import com.ramon.ramondrive.testing.unused

internal class FakeTransferRepository : TransferRepository by unused() {
    val downloads = mutableListOf<String>()

    override suspend fun enqueueDownload(fileId: String, priority: Int): AppResult<String> {
        downloads += fileId
        return AppResult.Success(fileId)
    }
}
