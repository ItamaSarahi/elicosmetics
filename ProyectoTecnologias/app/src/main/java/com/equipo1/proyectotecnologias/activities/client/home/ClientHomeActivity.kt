package com.equipo1.proyectotecnologias.activities.client.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.equipo1.proyectotecnologias.R
import com.equipo1.proyectotecnologias.fragments.client.ClientCategoriesFragment
import com.equipo1.proyectotecnologias.fragments.client.ClientOrdersFragment
import com.equipo1.proyectotecnologias.fragments.client.ClientProfileFragment
import com.equipo1.proyectotecnologias.models.User
import com.equipo1.proyectotecnologias.utils.SharedPreg
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

class ClientHomeActivity : AppCompatActivity() {
    private val TAG ="ClienteHomeActivity"
    var sharedPreg:SharedPreg?=null

    var bottonNavigation: BottomNavigationView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "ELICOSMETICS"
        setContentView(R.layout.activity_client_home)
        sharedPreg= SharedPreg(this)
        getUserFromSession()

        openFragment(ClientCategoriesFragment())
        bottonNavigation= findViewById(R.id.bottom_navigation)
        bottonNavigation?.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.item_home -> {
                    openFragment(ClientCategoriesFragment())
                    true
                }

                R.id.item_orders -> {
                    openFragment(ClientOrdersFragment())
                    true
                }

                R.id.item_profile -> {
                    openFragment(ClientProfileFragment())
                    true
                }

                else -> false

            }

        }
    }

    private fun openFragment(fragment: Fragment){
       val transaction = supportFragmentManager.beginTransaction()
       transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }



    //obtneer el usuario de la sesion

    private fun getUserFromSession(){
        val gson = Gson()
        if(!sharedPreg?.getData("user").isNullOrBlank()){
            //Si el usuario existe en sesion
            val user = gson.fromJson(sharedPreg?.getData("user"), User::class.java)
            Log.d(TAG,"Usuario: $user")
        }

    }
}