package com.ramon.ramondrive.core.files

import org.junit.Assert.assertEquals
import org.junit.Test

class BulkRenameTest {

    @Test
    fun `numbering keeps the original extension`() {
        assertEquals("IMG_1.jpg", BulkRename.nameFor("IMG_{n}", "photo.jpg", 1))
        assertEquals("IMG_2.png", BulkRename.nameFor("IMG_{n}", "photo.png", 2))
    }

    @Test
    fun `name placeholder replaces the base name`() {
        assertEquals("ferias_2024.jpg", BulkRename.nameFor("{name}_2024", "ferias.jpg", 1))
    }

    @Test
    fun `extension placeholder can move the extension`() {
        assertEquals("jpg_photo.jpg", BulkRename.nameFor("{ext}_{name}", "photo.jpg", 1))
    }

    @Test
    fun `an explicit extension in the pattern wins`() {
        assertEquals("backup.txt", BulkRename.nameFor("backup.txt", "photo.jpg", 1))
    }

    @Test
    fun `files without extension stay untouched`() {
        assertEquals("README_1", BulkRename.nameFor("{name}_{n}", "README", 1))
    }

    @Test
    fun `illegal characters are replaced`() {
        assertEquals("a_b_c.txt", BulkRename.nameFor("a/b:c", "note.txt", 1))
    }

    @Test
    fun `dotfiles keep their leading dot`() {
        assertEquals(".env_1", BulkRename.nameFor("{name}_{n}", ".env", 1))
    }

    @Test
    fun `blank pattern keeps the original name`() {
        assertEquals("photo.jpg", BulkRename.nameFor("   ", "photo.jpg", 1))
    }
}
