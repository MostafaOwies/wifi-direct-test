package com.example.wifidirecttest

import android.net.wifi.p2p.WifiP2pDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.wifidirecttest.databinding.PeersListItemBinding


class ItemAdapter(private val items : MutableList<WifiP2pDevice>
): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private lateinit var mOnClickListener: OnClickListener
    interface OnClickListener {
        fun onClick(position: Int)
    }
    fun setOnClickListener(onClickListener: OnClickListener){
        mOnClickListener=onClickListener
    }

    class ViewHolder (itemView: View,listener:OnClickListener):RecyclerView.ViewHolder(itemView){
        val deviceName:TextView=itemView.findViewById(R.id.device_name)
        init {
            itemView.setOnClickListener{
                listener.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.peers_list_item,parent,false)
        return ViewHolder(itemView,mOnClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.deviceName.text=item.deviceName
    }

    override fun getItemCount(): Int {
        return items.size
    }
}