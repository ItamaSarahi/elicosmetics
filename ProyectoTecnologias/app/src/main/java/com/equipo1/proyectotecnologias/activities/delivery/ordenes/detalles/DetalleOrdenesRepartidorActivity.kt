package com.equipo1.proyectotecnologias.activities.delivery.ordenes.detalles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.equipo1.proyectotecnologias.R
import com.equipo1.proyectotecnologias.activities.delivery.ordenes.map.DeliveryOrdersMapActivityActivity
import com.equipo1.proyectotecnologias.activities.makeup.home.MakeupHomeActivity
import com.equipo1.proyectotecnologias.adapters.OrdenesProductosAdapter
import com.equipo1.proyectotecnologias.models.Order
import com.equipo1.proyectotecnologias.models.ResponseHttp
import com.equipo1.proyectotecnologias.models.User
import com.equipo1.proyectotecnologias.providers.OrdersProvider
import com.equipo1.proyectotecnologias.providers.UsersProvider
import com.equipo1.proyectotecnologias.utils.SharedPreg
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalleOrdenesRepartidorActivity : AppCompatActivity() {

    val TAG = "DetalleRepartidor"
    var order: Order? = null
    val gson = Gson()

    var toolbar: Toolbar? = null
    var textViewClient: TextView? = null
    var textViewAddress: TextView? = null
    var textViewDate: TextView? = null
    var textViewTotal: TextView? = null
    var textViewStatus: TextView? = null
    var recyclerViewProducts: RecyclerView? = null
    var buttonUpdate: Button? = null
    var buttonMap: Button?=null

    var adapter: OrdenesProductosAdapter? = null

    var usersProvider: UsersProvider? = null
    var ordersProvider: OrdersProvider? = null
    var user: User? = null
    var sharedPref: SharedPreg? = null

    var idDelivery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_ordenes_repartidor)

        sharedPref = SharedPreg(this)

        order = gson.fromJson(intent.getStringExtra("order"), Order::class.java)

        getUserFromSession()

        usersProvider = UsersProvider()
        ordersProvider = OrdersProvider()

        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        toolbar?.title = "Order #${order?.id}"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textViewClient = findViewById(R.id.textview_client)
        textViewAddress = findViewById(R.id.textview_address)
        textViewDate = findViewById(R.id.textview_date)
        textViewTotal = findViewById(R.id.textview_total)
        textViewStatus = findViewById(R.id.textview_status)
        buttonUpdate = findViewById(R.id.btn_update)
        buttonMap = findViewById(R.id.btn_mapa)

        recyclerViewProducts = findViewById(R.id.recyclerview_products)
        recyclerViewProducts?.layoutManager = LinearLayoutManager(this)

        adapter = OrdenesProductosAdapter(this, order?.products!!)
        recyclerViewProducts?.adapter = adapter

        textViewClient?.text = "${order?.client?.nombre} ${order?.client?.apellido}"
        textViewAddress?.text = order?.address?.address
        textViewDate?.text = "${order?.timestamp}"
        textViewStatus?.text = order?.status

        Log.d(TAG, "Orden: ${order.toString()}")

        getTotal()

        if (order?.status == "DESPACHADO") {
            buttonUpdate?.visibility = View.VISIBLE
        }

        if (order?.status == "EN CAMINO") {
            buttonMap?.visibility = View.VISIBLE
        }

        buttonUpdate?.setOnClickListener{updateOrder()}
        buttonMap?.setOnClickListener{goToMap()}

    }



    private fun updateOrder() {

        ordersProvider?.updateToOnTheWay(order!!)?.enqueue(object: Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                if (response.body() != null) {


                    if (response.body()?.isSuccess == true) {
                        Toast.makeText(this@DetalleOrdenesRepartidorActivity, "ENTREGA INICIADA", Toast.LENGTH_LONG).show()
                        goToMap()
                    }
                    else {
                        Toast.makeText(this@DetalleOrdenesRepartidorActivity, "No se pudo asignar el repartidor", Toast.LENGTH_LONG).show()
                    }
                }
                else {
                    Toast.makeText(this@DetalleOrdenesRepartidorActivity, "No hubo respuesta del servidor", Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@DetalleOrdenesRepartidorActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })

    }


    private fun goToMap() {
        val i = Intent(this, DeliveryOrdersMapActivityActivity::class.java)
        i.putExtra("order",order?.toJson())
        startActivity(i)
    }


    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            // SI EL USARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }
    private fun getTotal() {
        var total = 0.0

        for (p in order?.products!!) {
            total = total + (p.price * p.quantity!!)
        }
        textViewTotal?.text = "${total}$"

    }
}