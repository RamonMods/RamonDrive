package com.ramon.ramondrive.presentation

import com.ramon.ramondrive.presentation.di.sharedUiModule
import com.ramon.ramondrive.presentation.platform.AndroidPlatformCapabilities
import com.ramon.ramondrive.presentation.platform.AndroidStandardFolderPaths
import com.ramon.ramondrive.presentation.platform.PlatformCapabilities
import com.ramon.ramondrive.presentation.platform.StandardFolderPaths
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val presentationModule = module {
    singleOf(::AndroidStandardFolderPaths) bind StandardFolderPaths::class
    singleOf(::AndroidPlatformCapabilities) bind PlatformCapabilities::class
    includes(sharedUiModule)
}
