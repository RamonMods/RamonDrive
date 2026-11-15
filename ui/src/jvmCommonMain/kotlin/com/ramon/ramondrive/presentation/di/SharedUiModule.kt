package com.ramon.ramondrive.presentation.di

import com.ramon.ramondrive.presentation.AppViewModel
import com.ramon.ramondrive.presentation.channels.ChannelsViewModel
import com.ramon.ramondrive.presentation.collection.CollectionViewModel
import com.ramon.ramondrive.presentation.duplicates.DuplicatesViewModel
import com.ramon.ramondrive.presentation.files.FileClipboard
import com.ramon.ramondrive.presentation.files.FilesViewModel
import com.ramon.ramondrive.presentation.gallery.GalleryViewModel
import com.ramon.ramondrive.presentation.home.HomeViewModel
import com.ramon.ramondrive.presentation.onboarding.OnboardingViewModel
import com.ramon.ramondrive.presentation.note.NoteEditorViewModel
import com.ramon.ramondrive.presentation.preview.PreviewContentResolver
import com.ramon.ramondrive.presentation.preview.PreviewViewModel
import com.ramon.ramondrive.presentation.proxy.ProxyViewModel
import com.ramon.ramondrive.presentation.search.SearchViewModel
import com.ramon.ramondrive.presentation.settings.ExclusionsViewModel
import com.ramon.ramondrive.presentation.settings.SettingsViewModel
import com.ramon.ramondrive.presentation.transfers.TransfersViewModel
import com.ramon.ramondrive.presentation.trash.TrashViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedUiModule = module {
    singleOf(::PreviewContentResolver)
    singleOf(::FileClipboard)
    viewModelOf(::AppViewModel)
    viewModelOf(::PreviewViewModel)
    viewModelOf(::ChannelsViewModel)
    viewModelOf(::CollectionViewModel)
    viewModelOf(::DuplicatesViewModel)
    viewModelOf(::FilesViewModel)
    viewModelOf(::GalleryViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::NoteEditorViewModel)
    viewModelOf(::ProxyViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::ExclusionsViewModel)
    viewModelOf(::TransfersViewModel)
    viewModelOf(::TrashViewModel)
}
