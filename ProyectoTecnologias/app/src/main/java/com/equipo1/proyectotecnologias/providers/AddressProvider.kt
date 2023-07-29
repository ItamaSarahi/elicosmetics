package com.equipo1.proyectotecnologias.providers

import com.equipo1.proyectotecnologias.api.ApiRoutes
import com.equipo1.proyectotecnologias.models.Address
import com.equipo1.proyectotecnologias.models.ResponseHttp
import com.equipo1.proyectotecnologias.routes.AddressRoutes
import retrofit2.Call

class AddressProvider() {

    private var addressRoutes: AddressRoutes? = null

    init {
        val api = ApiRoutes()
        addressRoutes = api.getAddresssRoutes()
    }

    fun getAddress(idUser: String): Call<ArrayList<Address>>? {
        return addressRoutes?.getAddress(idUser)
    }


    fun create(address: Address): Call<ResponseHttp>? {
        return addressRoutes?.create(address)
    }

}