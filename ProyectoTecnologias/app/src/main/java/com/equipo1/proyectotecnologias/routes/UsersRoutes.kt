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

interface UsersRoutes {


    @GET("api/users/findDelivary")
    fun getDelivary():Call<ArrayList<User>>

    @POST("api/users/create")
    fun register(@Body user:User): Call<ResponseHttp>

    @FormUrlEncoded
    @POST("api/users/login")
    fun login (@Field("email")email:String,@Field("password")password:String):Call<ResponseHttp>


    //metodo que permite actualizar datos
    @Multipart
    @PUT("api/users/update")
    fun update(
        @Part image: MultipartBody.Part,
        @Part("user") user: RequestBody
    ): Call<ResponseHttp>

    @PUT("api/users/updateWithoutImage")
    fun updateWithoutImage(
        @Body user: User,
    ): Call<ResponseHttp>

}