package com.equipo1.proyectotecnologias.providers

import android.util.Log
import com.equipo1.proyectotecnologias.api.ApiRoutes
import com.equipo1.proyectotecnologias.models.Order
import com.equipo1.proyectotecnologias.models.ResponseHttp
import com.equipo1.proyectotecnologias.routes.OrdersRoutes
import retrofit2.Call

class OrdersProvider()  {

    private var ordersRoutes: OrdersRoutes? = null

    init {
        val api = ApiRoutes()
        ordersRoutes = api.getOrdersRoutes()
    }


    fun getOrdersByStatus(status: String): Call<ArrayList<Order>>? {
        Log.d("","ENTRA AQUI O NO ORDERS PROVIDER")
        return ordersRoutes?.getOrdersByStatus(status)
    }

    fun getOrdersByClientAndStatus(idClient: String, status: String): Call<ArrayList<Order>>? {
            return ordersRoutes?.getOrdersByClientAndStatus(idClient, status)
    }

    fun getOrdersByDeliveryAndStatus(idDelivery: String, status: String): Call<ArrayList<Order>>? {
               return ordersRoutes?.getOrdersByDeliveryAndStatus(idDelivery, status)
    }

    fun create(order: Order): Call<ResponseHttp>? {
        return ordersRoutes?.create(order)
    }


    fun updateToDispatched(order: Order): Call<ResponseHttp>? {
        return ordersRoutes?.updateToDispatched(order)
    }

    fun updateToOnTheWay(order: Order): Call<ResponseHttp>? {
        return ordersRoutes?.updateToOnTheWay(order)
    }

    fun updateToDelivered(order: Order): Call<ResponseHttp>? {
        return ordersRoutes?.updateToDelivered(order)
    }

    fun updateLatLng(order: Order): Call<ResponseHttp>? {
           return ordersRoutes?.updateLatLng(order)
    }



}