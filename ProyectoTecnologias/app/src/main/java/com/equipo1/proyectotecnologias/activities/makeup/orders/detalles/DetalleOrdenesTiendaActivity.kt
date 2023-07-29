package com.equipo1.proyectotecnologias.activities.makeup.orders.detalles

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

class DetalleOrdenesTiendaActivity : AppCompatActivity() {

    val TAG = "ClientOrdersDetail"
    var order: Order? = null
    val gson = Gson()

    var toolbar: Toolbar? = null
    var textViewClient: TextView? = null
    var textViewAddress: TextView? = null
    var textViewDate: TextView? = null
    var textViewTotal: TextView? = null
    var textViewStatus: TextView? = null
    var textViewDelivery: TextView? = null
    var textViewDeliveryAssigned: TextView? = null
    var textViewDeliveryAvailable: TextView? = null
    var recyclerViewProducts: RecyclerView? = null
    var buttonUpdate: Button? = null

    var adapter: OrdenesProductosAdapter? = null

    var usersProvider: UsersProvider? = null
    var ordersProvider: OrdersProvider? = null
    var user: User? = null
    var sharedPref: SharedPreg? = null

    var spinnerRepartidores: Spinner? = null
    var idDelivery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_ordenes_tienda)

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
        textViewDelivery = findViewById(R.id.textview_delivery)
        textViewDeliveryAssigned = findViewById(R.id.textview_delivery_assigned)
        textViewDeliveryAvailable = findViewById(R.id.textview_delivery_available)
        spinnerRepartidores = findViewById(R.id.spinner_delivery_men)
        buttonUpdate = findViewById(R.id.btn_update)

        recyclerViewProducts = findViewById(R.id.recyclerview_products)
        recyclerViewProducts?.layoutManager = LinearLayoutManager(this)

        adapter = OrdenesProductosAdapter(this, order?.products!!)
        recyclerViewProducts?.adapter = adapter

        textViewClient?.text = "${order?.client?.nombre} ${order?.client?.apellido}"
        textViewAddress?.text = order?.address?.address
        textViewDate?.text = "${order?.timestamp}"
        textViewStatus?.text = order?.status
        textViewDelivery?.text = "${order?.delivery?.nombre} ${order?.delivery?.apellido}"

        Log.d(TAG, "Orden: ${order.toString()}")

        getTotal()
        getDeliveryMen()

        if (order?.status == "PAGADO") {
            buttonUpdate?.visibility = View.VISIBLE
            textViewDeliveryAvailable?.visibility = View.VISIBLE
            spinnerRepartidores?.visibility = View.VISIBLE
        }

        if (order?.status != "PAGADO") {
            textViewDeliveryAssigned?.visibility = View.VISIBLE
            textViewDelivery?.visibility = View.VISIBLE
        }


        buttonUpdate?.setOnClickListener{updateOrder()}

    }



    private fun updateOrder() {
        order?.idDelivery = idDelivery

        ordersProvider?.updateToDispatched(order!!)?.enqueue(object: Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                if (response.body() != null) {


                    if (response.body()?.isSuccess == true) {
                        Toast.makeText(this@DetalleOrdenesTiendaActivity, "Repartidor asignado correctamente", Toast.LENGTH_SHORT).show()
                        goToOrders()
                    }
                    else {
                        Toast.makeText(this@DetalleOrdenesTiendaActivity, "No se pudo asignar el repartidor", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Toast.makeText(this@DetalleOrdenesTiendaActivity, "No hubo respuesta del servidor", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@DetalleOrdenesTiendaActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })

    }


    private fun goToOrders() {
        val i = Intent(this, MakeupHomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }


    private fun getDeliveryMen() {
        usersProvider?.getDelivary()?.enqueue(object: Callback<ArrayList<User>> {
            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {

                if (response.body() != null) {
                    val repartidores = response.body()
                    val arrayAdapter = ArrayAdapter<User>(this@DetalleOrdenesTiendaActivity, android.R.layout.simple_dropdown_item_1line, repartidores!!)
                    spinnerRepartidores?.adapter = arrayAdapter
                    spinnerRepartidores?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, l: Long) {
                            idDelivery = repartidores[position].id!! //REPARTIDOR
                            Log.d(TAG, "Id Delivery: $idDelivery")
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }
                    }

                }

            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Toast.makeText(this@DetalleOrdenesTiendaActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
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