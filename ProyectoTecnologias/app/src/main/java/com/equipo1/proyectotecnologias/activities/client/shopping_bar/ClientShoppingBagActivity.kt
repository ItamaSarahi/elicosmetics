package com.equipo1.proyectotecnologias.activities.client.shopping_bar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.equipo1.proyectotecnologias.R
import com.equipo1.proyectotecnologias.activities.client.adress.create.ClientAdressCreateActivity
import com.equipo1.proyectotecnologias.activities.client.adress.list.ClientAddressListActivity
import com.equipo1.proyectotecnologias.adapters.ShoppingBagAdapter
import com.equipo1.proyectotecnologias.models.Product
import com.equipo1.proyectotecnologias.utils.SharedPreg
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ClientShoppingBagActivity : AppCompatActivity() {

    var recyclerViewShoppingBag: RecyclerView? = null
    var textViewTotal: TextView? = null
    var buttonNext: Button? = null
    var toolbar: androidx.appcompat.widget.Toolbar? = null
    var adapter: ShoppingBagAdapter? = null
    var sharedPref: SharedPreg? = null
    var gson = Gson()
    var selectedProducts = ArrayList<Product>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_shooping_bag)

        sharedPref = SharedPreg(this)

        recyclerViewShoppingBag = findViewById(R.id.recyclerview_shopping_bag)
        textViewTotal = findViewById(R.id.textview_total)
        buttonNext = findViewById(R.id.btn_nexting)
        toolbar = findViewById(R.id.toolbar)

        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        toolbar?.title = "Tu orden"

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerViewShoppingBag?.layoutManager = LinearLayoutManager(this)

        getProductsFromSharedPref()

        buttonNext?.setOnClickListener { goToAddressList() }
    }


    private fun goToAddressList() {
        val i = Intent(this, ClientAddressListActivity::class.java)
        startActivity(i)
    }



    fun setTotal(total: Double) {
        textViewTotal?.text = "${total}$"
    }

    private fun getProductsFromSharedPref() {

        if (!sharedPref?.getData("order").isNullOrBlank()) { // EXISTE UNA ORDEN EN SHARED PREF
            val type = object: TypeToken<ArrayList<Product>>() {}.type
            selectedProducts = gson.fromJson(sharedPref?.getData("order"), type)

            adapter = ShoppingBagAdapter(this, selectedProducts)
            recyclerViewShoppingBag?.adapter = adapter
        }

    }
}