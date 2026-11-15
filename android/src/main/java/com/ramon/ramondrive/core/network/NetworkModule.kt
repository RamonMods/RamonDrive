package com.ramon.ramondrive.core.network

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {
    singleOf(::ConnectivityNetworkMonitor) bind NetworkMonitor::class
}
