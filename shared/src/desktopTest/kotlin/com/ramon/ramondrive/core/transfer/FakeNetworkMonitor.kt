package com.ramon.ramondrive.core.transfer

import com.ramon.ramondrive.core.network.NetworkMonitor
import com.ramon.ramondrive.core.network.NetworkStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class FakeNetworkMonitor(initial: NetworkStatus) : NetworkMonitor {
    private val state = MutableStateFlow(initial)
    override val status: Flow<NetworkStatus> = state
    override fun currentStatus(): NetworkStatus = state.value
}
