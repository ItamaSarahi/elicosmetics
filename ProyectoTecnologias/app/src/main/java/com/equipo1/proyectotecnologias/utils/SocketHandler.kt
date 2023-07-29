package com.equipo1.proyectotecnologias.utils
import  com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import android.util.Log
import java.net.URISyntaxException

object SocketHandler {

    lateinit var mSocket: com.github.nkzawa.socketio.client.Socket

    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket("https://emergentes2.onrender.com/orders/delivery")
        } catch (e: URISyntaxException) {
            Log.d("Error", "No se pudo conectar el socket ${e.message}")
        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }


}