package com.example.wifidirecttest

import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wifidirecttest.databinding.PeersListItemBinding


class ItemAdapter(private val items : Array<String>
): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    class ViewHolder (binding:PeersListItemBinding):RecyclerView.ViewHolder(
        binding.root){
        val deviceName=binding.deviceName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            PeersListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]

        holder.deviceName.text=item
        if (onClickListener != null) {
            onClickListener!!.onClick(position, item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
    interface OnClickListener {
        fun onClick(position: Int, item: String)
    }
    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener=onClickListener
    }

}