package com.example.habits.adapter

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.R

@SuppressLint("MissingPermission")
class ContactItemAdapter(
    private val contactClickListener: (BluetoothDevice) -> Unit,
) : ListAdapter<BluetoothDevice, ContactItemAdapter.ContactViewHolder>(ContactComparator()) {

    class ContactViewHolder(
        private val view: View,
        private val clickAtPosition: (Int) -> Unit,
    ) : RecyclerView.ViewHolder(view) {
        private val contactName: TextView = view.findViewById(R.id.tvContactName)

        fun bindText(contact: String) {
            contactName.text = contact
        }

        init {
            contactName.setOnClickListener {
                clickAtPosition(adapterPosition)
                println("Hello")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.bluetooth_item, parent, false)
        return ContactViewHolder(view) { contactClickListener(getItem(it)) }
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindText(item.name)
    }
}

@SuppressLint("MissingPermission")
class ContactComparator : DiffUtil.ItemCallback<BluetoothDevice>() {

    override fun areItemsTheSame(oldDevice: BluetoothDevice, newDevice: BluetoothDevice): Boolean {
        return oldDevice.name == newDevice.name
    }

    override fun areContentsTheSame(oldDevice: BluetoothDevice, newDevice: BluetoothDevice): Boolean {
        return oldDevice.name == newDevice.name
    }
}