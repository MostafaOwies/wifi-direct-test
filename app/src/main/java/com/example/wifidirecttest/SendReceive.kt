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

class SendReceive( private  var socket: Socket?=null,
                   private  var inputStream: InputStream?=null,
                   private  var outputStream: OutputStream?=null
                   ) {


    suspend fun sendReceive(){
        withContext(Dispatchers.Default) {
            try {
                inputStream = socket!!.getInputStream()
                outputStream=socket!!.getOutputStream()

              var  buffer = byteArrayOf(1024.toByte())
                var bytes :Int

                while (socket!=null){
                    try {
                        bytes=inputStream!!.read(buffer)
                        if (bytes>0){
                            MainActivity().handler.obtainMessage(Constants.MESSAGE_READ,bytes,-1,buffer).sendToTarget()
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            } catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun write(bytes : ByteArray) {
        try {
            outputStream!!.write(bytes)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}