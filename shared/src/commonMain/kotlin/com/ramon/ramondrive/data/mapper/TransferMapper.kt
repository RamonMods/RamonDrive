package com.ramon.ramondrive.data.mapper

import com.ramon.ramondrive.data.local.entity.TransferEntity
import com.ramon.ramondrive.domain.model.TransferTask

fun TransferEntity.toDomain(): TransferTask = TransferTask(
    id = id,
    type = type,
    fileId = fileId,
    displayName = displayName,
    sizeBytes = sizeBytes,
    transferredBytes = transferredBytes,
    state = state,
    priority = priority,
    errorMessage = errorMessage,
    speedBytesPerSecond = speedBytesPerSecond,
    stage = stage,
    createdAt = createdAt,
    updatedAt = updatedAt,
    completedAt = completedAt
)
