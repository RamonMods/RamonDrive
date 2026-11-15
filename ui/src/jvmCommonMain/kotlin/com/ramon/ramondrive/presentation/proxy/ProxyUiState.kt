package com.ramon.ramondrive.presentation.proxy

import com.ramon.ramondrive.domain.model.ProxyServer

data class ProxyUiState(
    val proxies: List<ProxyServer> = emptyList(),
    val enabled: Boolean = false,
    val loading: Boolean = true,
    val reachability: Map<String, ProxyReachability> = emptyMap()
)
