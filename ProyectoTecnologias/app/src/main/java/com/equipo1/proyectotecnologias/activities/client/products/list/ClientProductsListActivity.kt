package com.equipo1.proyectotecnologias.activities.client.products.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.equipo1.proyectotecnologias.R
import com.equipo1.proyectotecnologias.adapters.ProductsAdapter
import com.equipo1.proyectotecnologias.models.Product
import com.equipo1.proyectotecnologias.models.User
import com.equipo1.proyectotecnologias.providers.ProductsProvider
import com.equipo1.proyectotecnologias.utils.SharedPreg
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Array

class ClientProductsListActivity : AppCompatActivity() {

    val TAG = "ClientProducts"
    var recyclerViewProducts: RecyclerView? = null
    var adapter: ProductsAdapter? = null

    var user: User? = null
    var sharedPref: SharedPreg? = null

    var productsProvider: ProductsProvider? = null
    var products: ArrayList<Product> = ArrayList()

    var idCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_list)

        sharedPref = SharedPreg(this)

        idCategory = intent.getStringExtra("idCategory")

        getUserFromSession()
        productsProvider = ProductsProvider()

        recyclerViewProducts = findViewById(R.id.recyclerview_products)
        recyclerViewProducts?.layoutManager = GridLayoutManager(this, 2)

        getProducts()
    }

    private fun getUserFromSession() {

        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()) {
            // SI EL USARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }

    }

    private fun getProducts() {
        productsProvider?.findByCategory(idCategory!!)?.enqueue(object: Callback<ArrayList<Product>> {
            override fun onResponse(
                call: Call<ArrayList<Product>>,
                response: Response<ArrayList<Product>>
            ) {

                if (response.body() != null) {
                    products = response.body()!!
                    adapter = ProductsAdapter(this@ClientProductsListActivity, products)
                    recyclerViewProducts?.adapter = adapter
                }

            }

            override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                Toast.makeText(this@ClientProductsListActivity, t.message, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Error: ${t.message}")
            }

        })
    }
}