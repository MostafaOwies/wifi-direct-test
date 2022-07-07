package com.example.wifidirecttest

import android.content.Context
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.ServerSocket
import java.net.Socket

class FileServer:Thread() {
    private lateinit var serverSocket:ServerSocket
      lateinit var socket:Socket
    private lateinit var sendReceive:SendReceive
    override fun run() {
        try {
            serverSocket =ServerSocket(8888)
            socket =serverSocket.accept()
            sendReceive= SendReceive(socket)
            sendReceive.start()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}