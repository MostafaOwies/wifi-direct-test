package com.example.wifidirecttest

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class FileClient ( private var mHostAddress:InetAddress? = null){
    private lateinit var socket:Socket
    private lateinit var host :String


    suspend fun clientSocket (){
        withContext(Dispatchers.Default) {
            host = mHostAddress?.hostAddress!!
            socket = Socket()
            try {
                socket.connect(InetSocketAddress(host, 8888), 500)
                SendReceive(socket).sendReceive()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}