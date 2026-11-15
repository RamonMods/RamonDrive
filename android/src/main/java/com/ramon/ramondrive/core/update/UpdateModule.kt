package com.ramon.ramondrive.core.update

import android.os.Build
import com.ramon.ramondrive.BuildConfig
import com.ramon.ramondrive.presentation.platform.UpdateInstaller
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val updateModule = module {
    single {
        UpdateChecker(
            currentVersion = BuildConfig.VERSION_NAME,
            abi = Build.SUPPORTED_ABIS.firstOrNull()
        )
    }
    singleOf(::AndroidUpdateInstaller) bind UpdateInstaller::class
    workerOf(::UpdateCheckWorker)
}
