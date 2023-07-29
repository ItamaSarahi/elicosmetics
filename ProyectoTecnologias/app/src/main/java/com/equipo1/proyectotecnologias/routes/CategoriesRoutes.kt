package com.equipo1.proyectotecnologias.routes

import com.equipo1.proyectotecnologias.models.Category
import com.equipo1.proyectotecnologias.models.ResponseHttp
import com.equipo1.proyectotecnologias.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface CategoriesRoutes {

    //metodo que permite actualizar datos
    @GET("api/categories/getAll")
    fun getAll():Call<ArrayList<Category>>




    @Multipart
    @POST("api/categories/create")
    fun create(
        @Part image: MultipartBody.Part,
        @Part("category") category: RequestBody
    ): Call<ResponseHttp>


}