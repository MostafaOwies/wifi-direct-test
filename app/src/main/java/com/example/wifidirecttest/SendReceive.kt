package com.example.wifidirecttest

import android.renderscript.ScriptGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception
import java.net.Socket
import java.util.zip.CheckedOutputStream
import java.util.zip.DeflaterInputStream

class SendReceive(
    private var socket: Socket,
    private var inputStream: InputStream,
    private var outputStream: OutputStream
) {

    suspend fun sendReceive(){
        withContext(Dispatchers.Default) {
            try {
                inputStream = socket.getInputStream()
                outputStream=socket.getOutputStream()

              var  buffer = byteArrayOf(1024.toByte())
                var bytes :Int
            } catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}