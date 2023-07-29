package com.equipo1.proyectotecnologias.activities.makeup.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.equipo1.proyectotecnologias.R
import com.equipo1.proyectotecnologias.activities.MainActivity
import com.equipo1.proyectotecnologias.fragments.client.ClientProfileFragment
import com.equipo1.proyectotecnologias.fragments.tienda.TiendaCategoryFragment
import com.equipo1.proyectotecnologias.fragments.tienda.ShopOrdersFragment
import com.equipo1.proyectotecnologias.fragments.tienda.TiendaProductFragment
import com.equipo1.proyectotecnologias.models.User
import com.equipo1.proyectotecnologias.utils.SharedPreg
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

class MakeupHomeActivity : AppCompatActivity() {
    private val TAG = "MakeupHomeActivity"
    var sharedPreg: SharedPreg? = null

    var bottonNavigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_makeup_home)
        sharedPreg = SharedPreg(this)
        supportActionBar?.title = "ELICOSMETICS"
        getUserFromSession()

        openFragment(ShopOrdersFragment())
        bottonNavigation = findViewById(R.id.bottom_navigation)
        bottonNavigation?.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.item_home -> {
                    openFragment(ShopOrdersFragment())
                    true
                }

                R.id.item_category -> {
                    openFragment(TiendaCategoryFragment())
                    true
                }

                R.id.item_products -> {
                    openFragment(TiendaProductFragment())
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

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun logout() {
        sharedPreg?.remove("user")
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    //obtneer el usuario de la sesion

    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPreg?.getData("user").isNullOrBlank()) {
            //Si el usuario existe en sesion
            val user = gson.fromJson(sharedPreg?.getData("user"), User::class.java)
            Log.d(TAG, "Usuario: $user")
        }

    }
}
