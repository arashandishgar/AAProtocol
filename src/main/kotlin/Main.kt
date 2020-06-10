import core.AAProtocol
import util.*
import java.io.File

//This file must be run for two times
fun main() {
    val byteCopiedListener = { size: Long, copy: Long ->
        printProgress(System.currentTimeMillis(), size, copy)
    }
    var currentHostStorePath: String?=null
    val file = File(BASE_STORE_PATH)
    if (!file.exists()) {
        file.mkdirs()
    }
    val aaObject = AAProtocol.getInstance {
        currentHostStorePath = when (it) {
            AAProtocol.TypeSocket.Server -> SERVER_STORE_PATH
            else -> CLIENT_STORE_PATH
        }
        File(currentHostStorePath!!).mkdirs()

    }
    Thread {
        println(USER_GUIDE_SEND_DATA_FORMAT)
        while (true) {
            val rawLine = readLine()
            try {
                val data = handleInputFrom(rawLine)
                when (data.first) {
                    AAProtocol.TypeData.TEXT -> aaObject.sendText(data.second)
                    AAProtocol.TypeData.File -> aaObject.sendFile(data.second, byteCopiedListener)
                }
            } catch (e: InvalidInputFormatException) {
                e.printStackTrace()
            }
        }
    }.start()
    Thread {
        while (true) {
            val listHeader = aaObject.readHeader()
            if (listHeader.get(CONTENT_TYPE_KEYWORD) == TEXT_TYPE) {
                aaObject.receiveText().println()
            } else {
                val fileSize = listHeader.get(FILE_SIZE_KEYWORD)!!.toLong()
                val fileName = listHeader.get(CONTENT_TYPE_KEYWORD)!!.split(SUBTYPE)[1]
                aaObject.receiveFile("$currentHostStorePath$fileName", fileSize, byteCopiedListener)
            }
        }
    }.start()
}

