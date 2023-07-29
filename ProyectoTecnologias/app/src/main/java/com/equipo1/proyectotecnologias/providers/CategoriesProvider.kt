package com.equipo1.proyectotecnologias.providers

import android.icu.util.ULocale.Category
import com.equipo1.proyectotecnologias.api.ApiRoutes
import com.equipo1.proyectotecnologias.models.ResponseHttp
import com.equipo1.proyectotecnologias.models.User
import com.equipo1.proyectotecnologias.routes.CategoriesRoutes
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class CategoriesProvider() {
    private var categoriesroutes: CategoriesRoutes?=null

    init{
        var api=ApiRoutes()
        categoriesroutes= api.getCategoriesRoutes()
    }


    fun getAll(): Call<ArrayList<com.equipo1.proyectotecnologias.models.Category>>?{
        return categoriesroutes?.getAll()
    }

    fun create(file: File, category: com.equipo1.proyectotecnologias.models.Category): Call<ResponseHttp>?{
        val reqFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val requestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), category.toJson())
        return categoriesroutes?.create(image,requestBody)
    }
}