package persist

import java.io.File

interface FileFactory {
    fun createFile(path: String): File
}

class FileFactoryImpl : FileFactory {
    override fun createFile(path: String): File = File(path)
}