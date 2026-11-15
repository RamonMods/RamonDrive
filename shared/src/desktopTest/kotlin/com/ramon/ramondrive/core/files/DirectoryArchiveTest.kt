package com.ramon.ramondrive.core.files

import java.io.File
import java.nio.file.Files
import java.util.zip.ZipFile
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DirectoryArchiveTest {

    private val root = Files.createTempDirectory("ramondrive-archive").toFile()

    @After
    fun tearDown() {
        root.deleteRecursively()
    }

    @Test
    fun `an empty folder keeps its root entry`() {
        val folder = File(root, "Docs").apply { mkdirs() }
        val staging = File(root, "staging").apply { mkdirs() }

        val archive = archiveDirectory(folder.absolutePath, staging)

        assertNotNull(archive)
        assertEquals("Docs.zip", archive!!.name)
        ZipFile(archive.path).use { zip ->
            assertEquals(setOf("Docs/"), zip.entries().asSequence().map { it.name }.toSet())
        }
    }

    @Test
    fun `files keep their relative paths`() {
        val folder = File(root, "Docs").apply { mkdirs() }
        File(folder, "a.txt").writeText("a")
        File(folder, "sub").mkdirs()
        File(folder, "sub/b.txt").writeText("b")
        File(folder, "empty").mkdirs()
        val staging = File(root, "staging").apply { mkdirs() }

        val archive = archiveDirectory(folder.absolutePath, staging)

        assertNotNull(archive)
        ZipFile(archive!!.path).use { zip ->
            assertEquals(
                setOf("Docs/", "Docs/a.txt", "Docs/sub/b.txt", "Docs/empty/"),
                zip.entries().asSequence().map { it.name }.toSet()
            )
            val text = zip.getInputStream(zip.getEntry("Docs/sub/b.txt"))
                .bufferedReader()
                .use { it.readText() }
            assertEquals("b", text)
        }
    }

    @Test
    fun `a staged name collision does not leak into the display name`() {
        val folder = File(root, "Docs").apply { mkdirs() }
        val staging = File(root, "staging").apply { mkdirs() }
        val existing = File(staging, "Docs.zip").apply { writeText("old") }

        val archive = archiveDirectory(folder.absolutePath, staging)

        assertNotNull(archive)
        assertEquals("Docs.zip", archive!!.name)
        assertNotEquals(existing.absolutePath, archive.path)
        assertEquals("old", existing.readText())
        assertTrue(File(archive.path).isFile)
    }

    @Test
    fun `the archive never includes itself`() {
        val folder = File(root, "Docs").apply { mkdirs() }
        File(folder, "a.txt").writeText("a")
        val staging = File(folder, "imports").apply { mkdirs() }

        val archive = archiveDirectory(folder.absolutePath, staging)

        assertNotNull(archive)
        ZipFile(archive!!.path).use { zip ->
            assertTrue(
                zip.entries().asSequence()
                    .none { it.name.startsWith("Docs/imports/ramondrive-") }
            )
        }
    }
}
