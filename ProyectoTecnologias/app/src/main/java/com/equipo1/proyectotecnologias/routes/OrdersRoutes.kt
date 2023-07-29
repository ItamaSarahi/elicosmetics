package com.equipo1.proyectotecnologias.routes

import com.equipo1.proyectotecnologias.models.Order
import com.equipo1.proyectotecnologias.models.ResponseHttp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrdersRoutes  {

        @GET("api/orders/findByStatus/{status}")
        fun getOrdersByStatus(
            @Path("status") status: String
        ): Call<ArrayList<Order>>

        @GET("api/orders/findByClientAndStatus/{id_client}/{status}")
           fun getOrdersByClientAndStatus(
               @Path("id_client") idClient: String,
               @Path("status") status: String,
        ): Call<ArrayList<Order>>


        @GET("api/orders/findByDeliveryAndStatus/{id_delivery}/{status}")
           fun getOrdersByDeliveryAndStatus(
               @Path("id_delivery") idDelivery: String,
               @Path("status") status: String,
        ): Call<ArrayList<Order>>

    @POST("api/orders/create")
    fun create(
        @Body order: Order
    ): Call<ResponseHttp>

    @PUT("api/orders/updateToDispatched")
    fun updateToDispatched(
        @Body order: Order,
    ): Call<ResponseHttp>

    @PUT("api/orders/updateToOnTheWay")
    fun updateToOnTheWay(
        @Body order: Order,
    ): Call<ResponseHttp>

    @PUT("api/orders/updateToDelivery")
    fun updateToDelivered(
        @Body order: Order,
    ): Call<ResponseHttp>

    @PUT("api/orders/updateLatLng")
        fun updateLatLng(
        @Body order: Order,
    ): Call<ResponseHttp>

}