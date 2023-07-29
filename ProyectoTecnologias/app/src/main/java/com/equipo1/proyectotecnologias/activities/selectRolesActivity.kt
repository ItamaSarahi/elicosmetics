package com.equipo1.proyectotecnologias.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.equipo1.proyectotecnologias.R
import com.equipo1.proyectotecnologias.adapters.RolesAdapter
import com.equipo1.proyectotecnologias.models.User
import com.equipo1.proyectotecnologias.utils.SharedPreg
import com.google.gson.Gson

class selectRolesActivity : AppCompatActivity() {

    var recyclerViewRoles: RecyclerView?=null
    var user: User?=null
    var adapter: RolesAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_roles)
        recyclerViewRoles= findViewById(R.id.recyclerview_roles)
        recyclerViewRoles?.layoutManager = LinearLayoutManager(this)
        supportActionBar?.title = "ELICOSMETICS"
        getUserFromSession()


        adapter=RolesAdapter(this, user?.roles!!)
        recyclerViewRoles?.adapter=adapter
    }

    private fun getUserFromSession(){

        val sharedPreg= SharedPreg(this)
        val gson = Gson()
        if(!sharedPreg.getData("user").isNullOrBlank()){
            //Si el usuario existe en sesion
            user = gson.fromJson(sharedPreg.getData("user"), User::class.java)
        }

    }
}