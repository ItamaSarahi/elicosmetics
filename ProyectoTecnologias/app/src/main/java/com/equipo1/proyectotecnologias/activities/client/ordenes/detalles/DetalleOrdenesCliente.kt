package com.equipo1.proyectotecnologias.activities.client.ordenes.detalles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.equipo1.proyectotecnologias.R
import com.equipo1.proyectotecnologias.activities.client.ordenes.map.ClientOrdersMainActivity
import com.equipo1.proyectotecnologias.activities.delivery.ordenes.map.DeliveryOrdersMapActivityActivity
import com.equipo1.proyectotecnologias.adapters.OrdenesProductosAdapter
import com.equipo1.proyectotecnologias.models.Order
import com.google.gson.Gson

class DetalleOrdenesCliente : AppCompatActivity() {

    val TAG = "ClientOrdersDetail"
    var order: Order? = null
    val gson = Gson()


    var toolbar: androidx.appcompat.widget.Toolbar? = null

    var textViewClient: TextView? = null
    var textViewAddress: TextView? = null
    var textViewDate: TextView? = null
    var textViewTotal: TextView? = null
    var textViewStatus: TextView? = null


    var recyclerViewProducts: RecyclerView? = null
    var adapter: OrdenesProductosAdapter? = null

    var buttonGoToMap: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_ordenes_cliente)

        order = gson.fromJson(intent.getStringExtra("order"), Order::class.java)
        Log.d(TAG, "Orden: ${order.toString()}")

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
        recyclerViewProducts = findViewById(R.id.recyclerview_products)
        recyclerViewProducts?.layoutManager = LinearLayoutManager(this)
        adapter = OrdenesProductosAdapter(this, order?.products!!)
        recyclerViewProducts?.adapter = adapter
        buttonGoToMap = findViewById(R.id.btn_go_to_map)

        textViewClient?.text = "${order?.client?.nombre} ${order?.client?.apellido}"
        textViewAddress?.text = order?.address?.address
        textViewDate?.text = "${order?.timestamp}"
        textViewStatus?.text = order?.status

        getTotal()

        if (order?.status == "EN CAMINO") {
            buttonGoToMap?.visibility = View.VISIBLE
        }



        buttonGoToMap?.setOnClickListener { goToMap() }
    }

    private fun getTotal() {
        var total = 0.0

        for (p in order?.products!!) {
            total = total + (p.price * p.quantity!!)
        }
        textViewTotal?.text = "${total}$"


    }

    private fun goToMap() {
        val i = Intent(this, ClientOrdersMainActivity::class.java)
        i.putExtra("order",order?.toJson())
        startActivity(i)
    }
}