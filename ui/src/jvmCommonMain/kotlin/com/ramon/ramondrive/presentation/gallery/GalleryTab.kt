package com.ramon.ramondrive.presentation.gallery

import org.jetbrains.compose.resources.StringResource
import com.ramon.ramondrive.resources.Res
import com.ramon.ramondrive.resources.gallery_tab_albums
import com.ramon.ramondrive.resources.gallery_tab_all
import com.ramon.ramondrive.resources.gallery_tab_photos
import com.ramon.ramondrive.resources.gallery_tab_videos

enum class GalleryTab(val labelRes: StringResource) {
    ALL(Res.string.gallery_tab_all),
    PHOTOS(Res.string.gallery_tab_photos),
    VIDEOS(Res.string.gallery_tab_videos),
    ALBUMS(Res.string.gallery_tab_albums)
}
