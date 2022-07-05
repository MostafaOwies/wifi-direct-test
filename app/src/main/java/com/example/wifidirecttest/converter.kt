package com.example.wifidirecttest

import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager.PeerListListener
import android.net.wifi.p2p.WifiP2pDeviceList
import java.util.ArrayList

class converter {
    var peers: MutableList<WifiP2pDevice> = ArrayList()
    private lateinit var deviceNameArray: Array<String?>
    private lateinit var deviceArray: Array<WifiP2pDevice?>
    var peerListListener = PeerListListener { peerList ->
        if (peerList.deviceList != peers) {
            peers.clear()
            peers.addAll(peerList.deviceList)
            deviceNameArray = arrayOf(peerList.deviceList.size.toString())
            deviceArray = arrayOfNulls(peerList.deviceList.size)
            val index = 0
        }
    }
}