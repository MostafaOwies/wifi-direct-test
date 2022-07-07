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

class SendReceive( private  var socket: Socket?=null) :Thread() {

    private  var inputStream: InputStream?=null
    private  var outputStream: OutputStream?=null

    init {
        try {
            inputStream = socket!!.getInputStream()
            outputStream=socket!!.getOutputStream()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun run() {

            val buffer = ByteArray(1024)
            var bytes :Int
            while (socket!=null){
                try {
                    bytes=inputStream!!.read(buffer)
                        MainActivity().handler.obtainMessage(Constants.MESSAGE_READ,bytes,-1,buffer).sendToTarget()
                }catch (e:Exception){
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