package com.ramon.ramondrive.data.mapper

import com.ramon.ramondrive.data.local.entity.BackupSessionEntity
import com.ramon.ramondrive.domain.model.BackupSession

fun BackupSessionEntity.toDomain(): BackupSession = BackupSession(
    id = id,
    trigger = trigger,
    status = status,
    totalFiles = totalFiles,
    completedFiles = completedFiles,
    failedFiles = failedFiles,
    skippedFiles = skippedFiles,
    totalBytes = totalBytes,
    transferredBytes = transferredBytes,
    startedAt = startedAt,
    completedAt = completedAt
)
