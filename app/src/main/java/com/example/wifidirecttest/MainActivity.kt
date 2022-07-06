package com.example.wifidirecttest

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.IntentFilter
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.ListAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wifidirecttest.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.net.InetAddress
import java.net.Socket

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private lateinit var mWifiP2pManager: WifiP2pManager
    private lateinit var channel: WifiP2pManager.Channel
    private var receiver: BroadcastReceiver? = null
    private var intentFilter = IntentFilter()

    private val peers = mutableListOf<WifiP2pDevice>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        initializeWifiP2P()
        searchForPeers()
        sendBtnClick()
    }


    val peerListListener = WifiP2pManager.PeerListListener { peerList ->
        val refreshedPeers = peerList.deviceList
        if (refreshedPeers != peers) {
            peers.clear()
            peers.addAll(refreshedPeers)
            initRecyclerView(peers)
        }

        if (peers.isEmpty()) {
            Log.d(TAG, "No devices found")
            return@PeerListListener
        }
    }

     val connectionListener = WifiP2pManager.ConnectionInfoListener { info ->

        val groupOwnerAddress: InetAddress? = info.groupOwnerAddress

        if (info.groupFormed && info.isGroupOwner) {
           lifecycleScope.launch{ FileServer().serverSocket()}
        } else if (info.groupFormed) {
            lifecycleScope.launch{FileClient(groupOwnerAddress).clientSocket()}
        }
    }

     var handler: Handler = Handler(object : Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            when (msg.what) {
                Constants.MESSAGE_READ -> {
                    val readBuff: ByteArray = msg.obj as ByteArray
                    val tempMessage = String(readBuff, 0, msg.arg1)
                    binding?.messegeTv?.setText(tempMessage)
                }
            }
            return true
        }
    })

    private fun sendBtnClick(){

        binding?.sendBtn?.setOnClickListener{
           val message :String = binding?.messegeEt?.text.toString()
            SendReceive().write(message.toByteArray())
        }
    }

    private fun initRecyclerView(peersList: MutableList<WifiP2pDevice>) {
        binding?.devicesRv?.layoutManager = LinearLayoutManager(this)
        val adapter = ItemAdapter(peersList)
        binding?.devicesRv?.adapter = adapter


        adapter.setOnClickListener(object : ItemAdapter.OnClickListener {
            @SuppressLint("MissingPermission")
            override fun onClick(position: Int) {
                Toast.makeText(this@MainActivity,"Device Clicked",Toast.LENGTH_SHORT).show()
                val device = peers[0]
                val config = WifiP2pConfig().apply {
                    deviceAddress = device.deviceAddress
                    wps.setup = WpsInfo.PBC
                }

                mWifiP2pManager.connect(channel, config, object : WifiP2pManager.ActionListener {
                    override fun onSuccess() {
                        Toast.makeText(
                            this@MainActivity,
                            "Connected to${device.deviceName}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onFailure(reason: Int) {
                        Toast.makeText(this@MainActivity, "not Connected ", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }
        })
    }



    private fun initializeWifiP2P() {
        mWifiP2pManager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = mWifiP2pManager.initialize(this, mainLooper, null)
        channel.also { channel ->
            receiver = WiFiDirectBroadcastReceiver(mWifiP2pManager, channel, this)
        }

        intentFilter = IntentFilter().apply {
            addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun searchForPeers() {
        binding?.searchBtn?.setOnClickListener {
            mWifiP2pManager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    Toast.makeText(applicationContext, "found", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(reasonCode: Int) {
                }
            })
        }
    }


    override fun onResume() {
        super.onResume()
        receiver?.also { receiver ->
            registerReceiver(receiver, intentFilter)
        }
    }

    override fun onPause() {
        super.onPause()
        receiver?.also { receiver ->
            unregisterReceiver(receiver)
        }
    }


}

