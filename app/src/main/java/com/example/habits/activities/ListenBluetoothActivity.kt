package com.example.habits.activities

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.habits.HabitApplication
import com.example.habits.HabitViewModel
import com.example.habits.HabitViewModelFactory
import com.example.habits.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.time.LocalDate
import java.util.*

class ListenBluetoothActivity : AppCompatActivity() {
    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory((application as HabitApplication).repository)
    }

    private val GEN_UUID = "4159c658-0218-11ee-be56-0242ac120002"
    var bluetoothAdapter: BluetoothAdapter? = null
    private var acceptThread: AcceptThread? = null
    var listen: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bluetooth_listen)

        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter
        val listenButton = findViewById<Button>(R.id.buttonListen)
        listenButton.setOnClickListener {
            if (listen) {
                acceptThread = AcceptThread()
                acceptThread!!.start()
                listen = false
                listenButton.text = "Stop listening"
            } else {
                listen = true
                acceptThread?.cancel()
                listenButton.text = "Start listening"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        acceptThread?.cancel()
    }

    inner class AcceptThread : Thread() {
        val threadStartedToast: Toast = Toast.makeText(this@ListenBluetoothActivity,"Started receiving", Toast.LENGTH_SHORT)

        private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            try {
                return@lazy bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord(
                    "HabitsApp", UUID.fromString(GEN_UUID)
                )
            } catch (e: SecurityException) {
                return@lazy null
            }
        }

        override fun run() {
            threadStartedToast.show()
            val shouldLoop = true
            while (shouldLoop) {
                val socket: BluetoothSocket? = try {
                    mmServerSocket?.accept()
                } catch (e: IOException) {
                    null
                }
                socket?.also {
                    val mmInStream: InputStream = it.inputStream
                    var fullPicture = ByteArray(0)
                    var fullSize = 0
                    val mmBuffer = ByteArray(1024)

                    try {
                        while (true) {
                            for (i in mmBuffer.indices)
                                mmBuffer[i] = 0
                            val numBytes = mmInStream.read(mmBuffer)
                            fullPicture += mmBuffer.take(numBytes).toByteArray()
                            if (numBytes == -1)
                                break
                            fullSize += numBytes
                        }
                    } catch (_: IOException) {
                        println("Input stream disconnected")
                    }
                    println("Success while reading from socket")

                    val photo = File(
                        this@ListenBluetoothActivity.getExternalFilesDir(Environment.DIRECTORY_DCIM)!!.absolutePath,
                        LocalDate.now().toString() + kotlin.random.Random.nextInt(0, 10000000)
                    )
                    if (photo.exists()) {
                        photo.delete()
                    }
                    val fos = FileOutputStream(photo.path)
                    fos.write(fullPicture)
                    fos.close()

                    val newPhoto = com.example.habits.data.Photo(
                        description = "No description",
                        path = photo.path,
                        habitId = -1,
                        day = 0,
                        doy = LocalDate.now().dayOfYear
                    )
                    habitViewModel.insertPhoto(newPhoto)
                }
            }
        }
        fun cancel() {
            mmServerSocket?.close()
        }
    }
}