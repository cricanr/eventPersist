package persist

import com.google.inject.Inject

interface IFilePersist {
    fun writeLine(message: String)
}

class FilePersist @Inject constructor(fileFactory: FileFactory) : IFilePersist {

    private val fileName = "events.txt"
    private val file = fileFactory.createFile(fileName)

    init {
        file.createNewFile()
    }

    override fun writeLine(message: String) {
        file.appendText(message + "\n")
    }
}