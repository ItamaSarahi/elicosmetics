package com.equipo1.proyectotecnologias.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class User(
    //mapear los datos que contiene la bd
       @SerializedName ("id") val id: String?=null,
       @SerializedName ("nombre") var nombre: String,
       @SerializedName ("apellido") var apellido: String,
       @SerializedName ("email") val email: String,
       @SerializedName ("telefono") var telefono: String,
       @SerializedName ("password") val password: String,
       @SerializedName ("image") var image: String?=null,
       @SerializedName("roles")val roles: ArrayList<Rol>?=null
) {
    override fun toString(): String {
        return "$nombre $apellido"
    }
    fun toJson(): String {
        return Gson().toJson(this)
    }
}