import java.io.*
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.security.MessageDigest
import java.time.*
import java.time.format.DateTimeFormatter
import kotlin.math.*

fun main(args: Array<String>) {
    val baseDir = File(if (args.isEmpty()) "." else args[0])
    if (!baseDir.isDirectory) return println("'$baseDir' is not a directory")
    if (!baseDir.exists()) return println("Directory '$baseDir' does not exist")
    println("Base dir: '$baseDir'")
    val expectedChecksumsFile = File("$baseDir/checksums.txt")
    val actualChecksumsFile = File("$baseDir/checksums-actual.txt")
    val files = (baseDir.listFiles() ?: return println("Failed to list files in '$baseDir'"))
        .filter { it.isFile && !it.name.startsWith(".") && it != expectedChecksumsFile && it != actualChecksumsFile }

    val checksums = files.sorted().asSequence()
        .onEach { print(".") }
        .map {
            val size = it.length().readableFileSize()
            val creationDate = fileCreationDate(it)
            val checksum = calculateChecksum(it)
            listOf(it.name, creationDate, size, checksum).joinToString(" - ")
        }
        .joinToString("\n")
        .also { println() }

    if (expectedChecksumsFile.exists()) {
        val expectedChecksums = expectedChecksumsFile.readText()
        if (expectedChecksums != checksums) {
            println("There were checksum differences")
            actualChecksumsFile.writeText(checksums)
            println("Checksums file created: ${actualChecksumsFile.absolutePath}")
        } else {
            println("Checksums match ${expectedChecksumsFile.absolutePath}")
        }
    } else {
        expectedChecksumsFile.writeText(checksums)
        println("Checksums file created: ${expectedChecksumsFile.absolutePath}")
    }
}

private fun calculateChecksum(file: File): String {
    val buffer = ByteArray(1024 * 8)
    FileInputStream(file).use { inputStream ->
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            digest.update(buffer, 0, bytesRead)
        }
    }
    return digest.digest().joinToString("") { "%02x".format(it) }
}

private fun Long.readableFileSize(): String {
    if (this == 0L) return "0B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB", "PB", "EB")
    val digitGroups = (log10(toDouble()) / log10(1024.0)).toInt()
    return String.format("%.1f%s", this / 1024.0.pow(digitGroups.toDouble()), units[digitGroups])
}

private fun fileCreationDate(file: File): String =
    Files.readAttributes(file.toPath(), BasicFileAttributes::class.java)
        .creationTime().toMillis()
        .let(Instant::ofEpochMilli)
        .let { formatter.format(it) }

private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault())

private val digest = MessageDigest.getInstance("SHA-256")
