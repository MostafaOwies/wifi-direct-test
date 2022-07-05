package com.example.wifidirecttest

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.wifi.p2p.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wifidirecttest.databinding.ActivityMainBinding
import java.net.InetAddress

class MainActivity : AppCompatActivity() {

    private var binding :ActivityMainBinding?=null
    val mWifiP2pManager: WifiP2pManager? by lazy(LazyThreadSafetyMode.NONE) {
        getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
    }

    var channel: WifiP2pManager.Channel? = null
    private var receiver: BroadcastReceiver? = null
    private var intentFilter = IntentFilter()

    private var peers =ArrayList<WifiP2pDevice>()
    private var deviceNameArray = arrayOf(String())
    private lateinit var deviceArray: Array<WifiP2pDevice?>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initRecyclerView()
        initializeWifiP2P()

    }

    private fun initRecyclerView(){
        binding?.devicesRv?.layoutManager=LinearLayoutManager(this)
        val adapter=ItemAdapter(deviceNameArray)
        binding?.devicesRv?.adapter=adapter
        adapter.setOnClickListener(object :ItemAdapter.OnClickListener{
            @SuppressLint("MissingPermission")
            override fun onClick(position: Int, item: String) {
                var device :WifiP2pDevice=deviceArray[position]!!
                val config = WifiP2pConfig()
                config.deviceAddress = device.deviceAddress
                channel?.also { channel ->
                    mWifiP2pManager?.connect(channel, config, object : WifiP2pManager.ActionListener {

                        override fun onSuccess() {
                            Toast.makeText(applicationContext,"Connected to${device.deviceName}",Toast.LENGTH_SHORT ).show()
                        }

                        override fun onFailure(reason: Int) {
                            Toast.makeText(applicationContext,"not Connected ",Toast.LENGTH_SHORT ).show()
                        }
                    }
                    )}
            }

        })
    }

    var peerListListener : WifiP2pManager.PeerListListener= object : WifiP2pManager.PeerListListener{
        override fun onPeersAvailable(peerList: WifiP2pDeviceList) {
            if (!peerList.deviceList.equals(peers)){
                peers.clear()
                peers.addAll(peerList.deviceList)
                deviceNameArray= arrayOf(peerList.deviceList.size.toString())
                deviceArray= arrayOfNulls(peerList.deviceList.size)
                var index = 0
                for (device:WifiP2pDevice in peerList.deviceList){
                    deviceNameArray[index]=device.deviceName
                    deviceArray[index]=device
                    index++
                }
            }
        }
    }

    private fun initializeWifiP2P(){
        channel = mWifiP2pManager?.initialize(this, mainLooper, null)
        channel?.also { channel ->
            receiver = WiFiDirectBroadcastReceiver(mWifiP2pManager!!, channel, this)
        }

        intentFilter = IntentFilter().apply {
            addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun searchForPeers(){
        binding?.searchBtn?.setOnClickListener {
            mWifiP2pManager?.discoverPeers(channel, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
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

