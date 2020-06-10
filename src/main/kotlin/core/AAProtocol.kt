package core

import util.*
import java.io.*
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket


class AAProtocol private constructor(private val socket: Socket) {
    private fun addBoundary(){
        addNewLineText(BOUNDARY)
    }
    private fun addHeaderText() {
        addNewLineText(TEXT_HEADER)
        addBoundary()
    }
    //fullName=name+format
    private fun addHeaderFile(fullName: String,size:Long) {
        addNewLineText("$FILE_HEADER_TYPE$fullName")
        addNewLineText("$FILE_HEADER_SIZE$size")
        addBoundary()
    }
    fun readHeader(): HashMap<String, String> {
        var line=""
        val hashMap=HashMap<String,String>()
        while (readNewLineText().let{line=it; line!= BOUNDARY }){
            val list=line.split(SPLITER).map { it.replace(" ","") }

            hashMap[list[0]] = list[1]
        }
        return hashMap
    }

    private fun readNewLineText() = bufferReader.readLine()

    private fun addNewLineText(message: String) {
        bufferWriter.write(message)
        bufferWriter.newLine()
        bufferWriter.flush()
    }

    fun sendFile(path: String, byteCopyStatus: (Long,Long) -> Unit) {
        val file=File(path)
       val fileInputStream= FileInputStream(file)
        val fileSize=fileInputStream.channel.size()
        addHeaderFile(file.name,fileSize)
        fileInputStream.copyTo(socket.getOutputStream(),fileSize,byteCopyStatus)
    }

    fun receiveFile(path: String,fileSize:Long,byteCopyStatus: (Long,Long) -> Unit) {
        socket.getInputStream().copyTo(FileOutputStream(path),fileSize,byteCopyStatus)
    }

    fun InputStream.copyTo(out: OutputStream,fileSize:Long, byteCopyStatus: (Long,Long) -> (Unit)) {
        val byteBuffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var size: Int
        var copied = 0L
        while (read(byteBuffer, 0, DEFAULT_BUFFER_SIZE).let { size = it;size >0 }) {
            out.write(byteBuffer,0,size)
            copied+=size
            byteCopyStatus(fileSize,copied)
            if(copied==fileSize){
                break
            }
        }
        out.flush()
        println("")
    }

    fun sendText(msg:String) {
        addHeaderText()
        addNewLineText(msg)
    }

    fun receiveText() = readNewLineText()
    enum class TypeData{
        TEXT,File
    }
    enum class TypeSocket{
        Server,Clinet
    }

    companion object {
        private var socket: core.AAProtocol? = null
        private val bufferReader: BufferedReader by lazy {
            BufferedReader(InputStreamReader(socket!!.socket.getInputStream()))
        }

        private val bufferWriter: BufferedWriter by lazy {
            BufferedWriter(OutputStreamWriter(socket!!.socket.getOutputStream()))
        }

        fun getInstance(onSuccessConnectListener: (TypeSocket) -> Unit): AAProtocol {
            socket = socket ?: AAProtocol(handleConnection(onSuccessConnectListener))
            return socket!!
        }

        private fun achiveRange(): String {
            val mainAddress = InetAddress.getLocalHost().hostAddress
            return mainAddress.split(".").let {
                val stringBuilder = StringBuilder()
                stringBuilder.append(it[0])
                stringBuilder.append(".")
                stringBuilder.append(it[1])
                stringBuilder.append(".")
                stringBuilder.append(it[2])
                stringBuilder.toString()
            }
        }

        private fun connectServer(): Socket? {
            var socket: Socket? = null
            produceIpFromRange(achiveRange()) {
                try {
                    val tempSocket = Socket()
                    tempSocket.connect(InetSocketAddress(it, PORT_Connect), 10)
                    socket = tempSocket
                } catch (e: Exception) {
                }
            }
            return socket
        }

        private fun produceIpFromRange(range: String, act: (String) -> Unit) {
            for (i in 0..255) {
                val ip = "$range.$i"
                act(ip)
            }
        }

        private fun runServer() = ServerSocket(PORT_Connect).accept()
        private fun handleConnection(onSuccessConnectListener:(TypeSocket) ->(Unit)): Socket {
            var socket = connectServer()
            if (socket == null) {
                socket = runServer()
                onSuccessConnectListener(TypeSocket.Server)
                "run".log()
            } else {
                onSuccessConnectListener(TypeSocket.Clinet)
                "connect".log()
            }
            return socket!!
        }
    }
}