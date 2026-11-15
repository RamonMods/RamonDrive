package com.ramon.ramondrive.di

import com.ramon.ramondrive.core.common.commonModule
import com.ramon.ramondrive.core.crypto.cryptoModule
import com.ramon.ramondrive.core.dispatchers.dispatchersModule
import com.ramon.ramondrive.core.files.filesModule
import com.ramon.ramondrive.core.files.sharedFilesModule
import com.ramon.ramondrive.core.logging.debugLogModule
import com.ramon.ramondrive.core.media.mediaModule
import com.ramon.ramondrive.core.network.networkModule
import com.ramon.ramondrive.core.power.powerModule
import com.ramon.ramondrive.core.permissions.permissionsModule
import com.ramon.ramondrive.core.proxy.proxyModule
import com.ramon.ramondrive.core.publish.publishModule
import com.ramon.ramondrive.core.security.securityModule
import com.ramon.ramondrive.core.telegram.telegramModule
import com.ramon.ramondrive.core.transfer.transferEngineModule
import com.ramon.ramondrive.core.transfer.transferModule
import com.ramon.ramondrive.core.update.updateModule
import com.ramon.ramondrive.data.local.database.daosModule
import com.ramon.ramondrive.data.local.database.databaseModule
import com.ramon.ramondrive.data.local.preferences.preferencesModule
import com.ramon.ramondrive.data.repository.localDataModule
import com.ramon.ramondrive.data.repository.repositoryModule
import com.ramon.ramondrive.domain.usecase.useCaseModule
import com.ramon.ramondrive.presentation.presentationModule

val appModules = listOf(
    commonModule,
    cryptoModule,
    dispatchersModule,
    filesModule,
    mediaModule,
    networkModule,
    powerModule,
    permissionsModule,
    proxyModule,
    publishModule,
    securityModule,
    telegramModule,
    transferModule,
    updateModule,
    databaseModule,
    daosModule,
    preferencesModule,
    repositoryModule,
    localDataModule,
    sharedFilesModule,
    debugLogModule,
    transferEngineModule,
    useCaseModule,
    presentationModule
)
