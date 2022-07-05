package com.example.wifidirecttest

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class FileClient(
    private var socket:Socket,
    private var host :String,
    private var mhostAddress:InetAddress?
) {

    suspend fun clientSocket (){
        withContext(Dispatchers.Default) {
            host = mhostAddress?.hostAddress!!
            socket = Socket()
            try {
                socket.connect(InetSocketAddress(host, 8888), 500)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}