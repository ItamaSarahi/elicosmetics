package com.equipo1.proyectotecnologias.api

import com.equipo1.proyectotecnologias.routes.AddressRoutes
import com.equipo1.proyectotecnologias.routes.CategoriesRoutes
import com.equipo1.proyectotecnologias.routes.OrdersRoutes
import com.equipo1.proyectotecnologias.routes.ProductsRoutes
import com.equipo1.proyectotecnologias.routes.UsersRoutes

class ApiRoutes {
    val API_URL= "https://emergentes2.onrender.com"


    val retrofit = RetrofitClient()

    fun getUsersRoutes(): UsersRoutes{
        return retrofit.getClien(API_URL).create(UsersRoutes::class.java)
    }

    fun getCategoriesRoutes(): CategoriesRoutes{
        return retrofit.getClien(API_URL).create(CategoriesRoutes::class.java)
    }

    fun getAddresssRoutes(): AddressRoutes{
        return retrofit.getClien(API_URL).create(AddressRoutes::class.java)
    }


    fun getOrdersRoutes(): OrdersRoutes{
        return retrofit.getClien(API_URL).create(OrdersRoutes::class.java)
    }


    fun getProductsRoutes(): ProductsRoutes{
        return retrofit.getClien(API_URL).create(ProductsRoutes::class.java)
    }

}