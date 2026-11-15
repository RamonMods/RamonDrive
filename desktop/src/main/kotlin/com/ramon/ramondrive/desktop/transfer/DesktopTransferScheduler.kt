package com.ramon.ramondrive.desktop.transfer

import com.ramon.ramondrive.core.dispatchers.DispatcherProvider
import com.ramon.ramondrive.core.transfer.TransferDrainResult
import com.ramon.ramondrive.core.transfer.TransferQueueDrainer
import com.ramon.ramondrive.core.transfer.TransferScheduler
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DesktopTransferScheduler(
    private val drainer: TransferQueueDrainer,
    dispatchers: DispatcherProvider
) : TransferScheduler {

    private val scope = CoroutineScope(SupervisorJob() + dispatchers.io)
    private val draining = AtomicBoolean(false)
    private val requested = AtomicBoolean(false)

    override fun kick(allowMetered: Boolean) {
        requested.set(true)
        if (!draining.compareAndSet(false, true)) return
        scope.launch {
            try {
                while (requested.getAndSet(false)) {
                    while (drainer.drain(
                            isStopped = { false },
                            onTerminalFailure = { }
                        ) == TransferDrainResult.INTERRUPTED
                    ) {
                        delay(RETRY_DELAY_SECONDS.seconds)
                    }
                }
            } finally {
                draining.set(false)
            }
            if (requested.get()) kick(allowMetered)
        }
    }

    override fun rekick(allowMetered: Boolean) {
        kick(allowMetered)
    }

    private companion object {
        const val RETRY_DELAY_SECONDS = 15
    }
}
