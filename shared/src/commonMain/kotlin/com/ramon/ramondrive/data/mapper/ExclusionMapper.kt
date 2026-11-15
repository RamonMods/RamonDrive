package com.ramon.ramondrive.data.mapper

import com.ramon.ramondrive.data.local.entity.ExclusionEntity
import com.ramon.ramondrive.domain.model.Exclusion

fun ExclusionEntity.toDomain(): Exclusion = Exclusion(
    id = id,
    type = type,
    value = value,
    enabled = enabled,
    createdAt = createdAt
)
