package com.example.wifidirecttest

import android.content.Context
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.ServerSocket
import java.net.Socket

class FileServer(
    private var serverSocket:ServerSocket
    , private var socket:Socket) {
    suspend fun serverSocket(){
        withContext(Dispatchers.Default){
            try {
             serverSocket =ServerSocket(8888)
             socket =serverSocket.accept()
            }catch (e:Exception){
            e.printStackTrace()
            }
        }
    }
}