package com.equipo1.proyectotecnologias.routes

import com.equipo1.proyectotecnologias.models.Address
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
import retrofit2.http.Path

interface AddressRoutes {


    @GET("api/address/findByUser/{id_user}")
    fun getAddress(
        @Path("id_user") idUser: String
    ):Call<ArrayList<Address>>


    @POST("api/address/create")
    fun create(
        @Body address: Address
    ): Call<ResponseHttp>


}