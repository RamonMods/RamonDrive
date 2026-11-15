package com.ramon.ramondrive.data.repository

import com.ramon.ramondrive.core.publish.PublishScheduler

internal class CountingScheduler : PublishScheduler {
    var kicks = 0
    override fun kick() {
        kicks++
    }
}
