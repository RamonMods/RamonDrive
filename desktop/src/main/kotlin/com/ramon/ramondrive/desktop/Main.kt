package com.ramon.ramondrive.desktop

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.key.Keyer
import coil3.request.Options
import coil3.request.crossfade
import com.ramon.ramondrive.core.common.SafeLog
import com.ramon.ramondrive.core.files.AppStoragePaths
import com.ramon.ramondrive.core.files.PendingShare
import com.ramon.ramondrive.core.media.ThumbnailFetcherFactory
import com.ramon.ramondrive.core.media.ThumbnailModel
import com.ramon.ramondrive.core.media.ThumbnailStore
import com.ramon.ramondrive.core.media.thumbnailCacheKey
import com.ramon.ramondrive.core.publish.PublishScheduler
import com.ramon.ramondrive.desktop.di.desktopModule
import com.ramon.ramondrive.desktop.resources.Res
import com.ramon.ramondrive.desktop.resources.Res as DesktopRes
import com.ramon.ramondrive.desktop.resources.app_already_running
import com.ramon.ramondrive.desktop.resources.app_icon
import com.ramon.ramondrive.desktop.ui.FullscreenController
import com.ramon.ramondrive.desktop.ui.LocalFullscreenController
import com.ramon.ramondrive.desktop.ui.ProvideDesktopPlatformActions
import com.ramon.ramondrive.desktop.window.WindowsTitleBar
import com.ramon.ramondrive.domain.model.AppTheme
import com.ramon.ramondrive.core.logging.DebugLogController
import com.ramon.ramondrive.data.repository.FolderOwnershipRepair
import com.ramon.ramondrive.domain.repository.FileRepository
import com.ramon.ramondrive.domain.repository.SettingsRepository
import com.ramon.ramondrive.domain.repository.TransferRepository
import com.ramon.ramondrive.domain.repository.TrashRepository
import com.ramon.ramondrive.presentation.RamonDriveApp
import com.ramon.ramondrive.presentation.theme.systemDarkTheme
import com.ramon.ramondrive.resources.Res as SharedRes
import com.ramon.ramondrive.resources.app_name
import java.awt.Dimension
import javax.swing.JOptionPane
import kotlin.system.exitProcess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin

fun main() {
    SafeLog.verbose = true
    startKoin {
        modules(desktopModule)
    }
    val storagePaths = getKoin().get<AppStoragePaths>()
    if (!SingleInstanceLock(storagePaths.filesDir).acquire()) {
        JOptionPane.showMessageDialog(
            null,
            runBlocking { getString(DesktopRes.string.app_already_running) },
            runBlocking { getString(SharedRes.string.app_name) },
            JOptionPane.INFORMATION_MESSAGE
        )
        exitProcess(0)
    }
    val backgroundScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    getKoin().get<DebugLogController>().start(backgroundScope, alwaysVerbose = true)
    backgroundScope.launch {
        runCatching { getKoin().get<TransferRepository>().recoverOrphanedTransfers() }
        runCatching { getKoin().get<FileRepository>().sweepImportOrphans() }
        runCatching { getKoin().get<FolderOwnershipRepair>().refileStrayFiles() }
        getKoin().get<PublishScheduler>().kick()
        runCatching { getKoin().get<TrashRepository>().repairTrashTree() }
    }
    application {
        val windowState = rememberWindowState(width = 1200.dp, height = 800.dp)
        val fullscreenController = remember {
            FullscreenController(
                isFullscreen = { windowState.placement == WindowPlacement.Fullscreen },
                toggle = {
                    windowState.placement =
                        if (windowState.placement == WindowPlacement.Fullscreen) {
                            WindowPlacement.Floating
                        } else {
                            WindowPlacement.Fullscreen
                        }
                }
            )
        }
        Window(
            onCloseRequest = ::exitApplication,
            title = stringResource(SharedRes.string.app_name),
            icon = painterResource(Res.drawable.app_icon),
            state = windowState
        ) {
            window.minimumSize = Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT)
            setSingletonImageLoaderFactory { context ->
                ImageLoader.Builder(context)
                    .components {
                        add(ThumbnailFetcherFactory(getKoin().get<ThumbnailStore>()))
                        add(Keyer<ThumbnailModel> { data, _: Options ->
                            thumbnailCacheKey(data.fileId)
                        })
                    }
                    .crossfade(true)
                    .build()
            }
            val settingsRepository = remember { getKoin().get<SettingsRepository>() }
            val theme by remember {
                settingsRepository.preferences.map { it.theme }.distinctUntilChanged()
            }.collectAsState(initial = AppTheme.SYSTEM)
            val systemDark = systemDarkTheme()
            val darkTitleBar = when (theme) {
                AppTheme.LIGHT -> false
                AppTheme.DARK, AppTheme.AMOLED -> true
                AppTheme.SYSTEM -> systemDark
            }
            LaunchedEffect(darkTitleBar) { WindowsTitleBar.setDark(window, darkTitleBar) }
            CompositionLocalProvider(LocalFullscreenController provides fullscreenController) {
                ProvideDesktopPlatformActions {
                    RamonDriveApp(
                        pendingShare = getKoin().get<PendingShare>(),
                        notificationDestination = null,
                        onDestinationHandled = {}
                    )
                }
            }
        }
    }
}

private const val MIN_WINDOW_WIDTH = 480
private const val MIN_WINDOW_HEIGHT = 600
