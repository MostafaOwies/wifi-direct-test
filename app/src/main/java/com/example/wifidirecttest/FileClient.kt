package com.example.wifidirecttest

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class FileClient ( mHostAddress:InetAddress):Thread() {
    private var socket: Socket = Socket()
    private var host: String = mHostAddress.hostAddress!!
    private lateinit var sendReceive:SendReceive

    override fun run() {
        try {
            socket.connect(InetSocketAddress(host, 8888), 500)
            sendReceive= SendReceive(socket)
            sendReceive.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}