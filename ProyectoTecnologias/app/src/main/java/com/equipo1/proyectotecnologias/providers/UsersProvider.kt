package com.equipo1.proyectotecnologias.providers

import android.util.Log
import com.equipo1.proyectotecnologias.api.ApiRoutes
import com.equipo1.proyectotecnologias.models.ResponseHttp
import com.equipo1.proyectotecnologias.models.User
import com.equipo1.proyectotecnologias.routes.UsersRoutes
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class UsersProvider() {
    private var usersRoutes: UsersRoutes? = null

    init {
        val api = ApiRoutes()
        usersRoutes = api.getUsersRoutes()
    }

    fun register(user: User): Call<ResponseHttp>? {
        return usersRoutes?.register(user)
    }


    fun login(email:String, password:String): Call<ResponseHttp>? {
        return usersRoutes?.login(email, password)
    }

    fun updateWithoutImage(user:User): Call<ResponseHttp>? {
        return usersRoutes?.updateWithoutImage(user)
    }

    fun update(file: File, user: User): Call<ResponseHttp>?{
        val reqFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val requestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), user.toJson())
        return usersRoutes?.update(image,requestBody)
    }

    fun getDelivary(): Call<ArrayList<com.equipo1.proyectotecnologias.models.User>>?{
        return usersRoutes?.getDelivary()
    }

}

