package com.example.habits.activities

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.R
import com.example.habits.adapter.ContactItemAdapter
import java.io.IOException
import java.io.OutputStream
import java.util.*

class PairedDevicesActivity : AppCompatActivity() {
    private val GEN_UUID = "4159c658-0218-11ee-be56-0242ac120002"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bluetooth_contacts)
        if (Build.VERSION.SDK_INT > 30 && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            finish()
            return
        }

        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter = bluetoothManager.adapter
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        val pairedDevicesList = pairedDevices?.toList() ?: listOf()

        val recyclerView = findViewById<RecyclerView>(R.id.rvBluetooth)

        val picture = intent.getByteArrayExtra("photoToSend")

        val adapter = ContactItemAdapter {
            Toast.makeText(this@PairedDevicesActivity, "Started sending message", Toast.LENGTH_SHORT).show()
            val connectedDevice = ConnectThread(it, picture)
            connectedDevice.start()
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter.submitList(pairedDevicesList)
    }

    @SuppressLint("MissingPermission")
    private inner class ConnectThread(device: BluetoothDevice, private val photo: ByteArray?) : Thread() {
        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            try {
                return@lazy device.createRfcommSocketToServiceRecord(UUID.fromString(GEN_UUID))
            } catch (e: SecurityException) {
                return@lazy null
            }
        }

        override fun run() {
            if(photo == null) {
                cancel()
            }

            mmSocket?.let { socket ->
                try {
                    socket.connect()
                } catch (e: Exception) {
                    return
                }
                val mmOutStream: OutputStream = socket.outputStream
                mmOutStream.write(photo)
            }

            sleep(5000)
            cancel()
        }

        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                return
            }
        }
    }
}