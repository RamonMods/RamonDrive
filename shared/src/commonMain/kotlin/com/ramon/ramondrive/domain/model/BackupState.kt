package com.ramon.ramondrive.domain.model

enum class BackupState {
    NONE,
    QUEUED,
    UPLOADING,
    BACKED_UP,
    FAILED
}
