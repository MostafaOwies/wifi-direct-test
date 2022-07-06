package com.example.wifidirecttest

import android.content.Context
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.ServerSocket
import java.net.Socket

class FileServer {
    private lateinit var serverSocket:ServerSocket
      lateinit var socket:Socket
    suspend fun serverSocket(){
        withContext(Dispatchers.Default){
            try {
             serverSocket =ServerSocket(8888)
             socket =serverSocket.accept()
                SendReceive(socket).sendReceive()
            }catch (e:Exception){
            e.printStackTrace()
            }
        }
    }
}