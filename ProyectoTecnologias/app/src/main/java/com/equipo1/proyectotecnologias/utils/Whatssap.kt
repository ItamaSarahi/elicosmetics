package com.equipo1.proyectotecnologias.utils

import android.os.AsyncTask
import android.util.Log
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException

class Whatssap(private val accessToken: String, private val jsonBody: String) : AsyncTask<Void, Void, Boolean>() {

    companion object {
        private const val TAG = "SendMessageTask"
        private val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
    }

    override fun doInBackground(vararg voids: Void): Boolean {
        val url = "https://graph.facebook.com/v17.0/108135109033564/messages"

        val client = OkHttpClient()

        val body = RequestBody.create(JSON, jsonBody)

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $accessToken")
            .post(body)
            .build()

        try {
            val response: Response = client.newCall(request).execute()
            return response.isSuccessful
        } catch (e: IOException) {
            Log.e(TAG, "Error en la solicitud: ${e.message}")
            e.printStackTrace()
        }
        return false
    }

    override fun onPostExecute(isSuccessful: Boolean) {
        if (isSuccessful) {
            // La solicitud fue exitosa
            Log.d(TAG, "Mensaje enviado correctamente.")
        } else {
            // La solicitud no fue exitosa
            Log.d(TAG, "Error al enviar el mensaje.")
        }
    }
}
