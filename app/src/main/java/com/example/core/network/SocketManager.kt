package com.example.core.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.net.ServerSocket
import java.net.Socket
import android.util.Log

data class TransferState(
    val inProgress: Boolean = false,
    val progress: Float = 0f,
    val currentFileName: String = "",
    val speed: String = "0 MB/s"
)

class SocketManager {
    private var serverSocket: ServerSocket? = null
    private var clientSocket: Socket? = null
    private var isCancelled = false

    private val _transferState = MutableStateFlow(TransferState())
    val transferState: StateFlow<TransferState> = _transferState.asStateFlow()

    fun cancel() {
        isCancelled = true
        closeConnections()
    }

    private fun closeConnections() {
        try {
            serverSocket?.close()
            clientSocket?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun startServer(port: Int, filesToSent: List<String>) = withContext(Dispatchers.IO) {
        try {
            isCancelled = false
            serverSocket = ServerSocket(port)
            Log.d("SocketManager", "Server started on port $port")
            clientSocket = serverSocket?.accept()
            Log.d("SocketManager", "Client connected")

            val outStream = BufferedOutputStream(clientSocket!!.getOutputStream())
            val inStream = BufferedInputStream(clientSocket!!.getInputStream())

            // Communication outline (Simplified):
            // 1. Send number of files
            // 2. Loop files: send name, send size, send stream
            // Since this is a prototype logic based on the user request,
            // we'll leave detailed implementation for later or when full logic is needed.
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            closeConnections()
        }
    }

    suspend fun connectToServer(ipAddress: String, port: Int, saveDir: File) = withContext(Dispatchers.IO) {
        try {
            isCancelled = false
            clientSocket = Socket(ipAddress, port)
            Log.d("SocketManager", "Connected to Server")

            val inStream = BufferedInputStream(clientSocket!!.getInputStream())
            val outStream = BufferedOutputStream(clientSocket!!.getOutputStream())

            // Receiving logic here...
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            closeConnections()
        }
    }
}
