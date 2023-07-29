package com.equipo1.proyectotecnologias.routes

import com.equipo1.proyectotecnologias.models.Category
import com.equipo1.proyectotecnologias.models.Product
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
import retrofit2.http.Path

interface ProductsRoutes {

    @GET("api/products/findByCategory/{id_category}")
    fun findByCategory(
        @Path("id_category")idCategory:String
    ):Call<ArrayList<Product>>

    @Multipart
    @POST("api/products/create")
    fun create(
        @Part images: Array<MultipartBody.Part?>,
        @Part("product") product: RequestBody
    ): Call<ResponseHttp>


}