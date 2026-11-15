package com.ramon.ramondrive.desktop.network

import com.ramon.ramondrive.core.network.NetworkMonitor
import com.ramon.ramondrive.core.network.NetworkStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class DesktopNetworkMonitor : NetworkMonitor {

    private val state = MutableStateFlow(NetworkStatus.UNMETERED)

    override val status: Flow<NetworkStatus> = state

    override fun currentStatus(): NetworkStatus = state.value
}
