package com.example.wifidirecttest

import android.net.wifi.p2p.WifiP2pDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ItemAdapter(private val items : MutableList<WifiP2pDevice>
): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private lateinit var mOnItemClickListener: OnItemClickListener
    interface OnItemClickListener {
        fun onClick(position: Int)
    }
    fun setOnClickListener(onItemClickListener: OnItemClickListener){
        mOnItemClickListener=onItemClickListener
    }

    class ViewHolder (itemView: View,listener:OnItemClickListener):RecyclerView.ViewHolder(itemView){
        val deviceName:TextView=itemView.findViewById(R.id.device_name)
        init {
            itemView.setOnClickListener{
                listener.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.peers_list_item,parent,false)
        return ViewHolder(itemView,mOnItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.deviceName.text=item.deviceName
    }

    override fun getItemCount(): Int {
        return items.size
    }
}