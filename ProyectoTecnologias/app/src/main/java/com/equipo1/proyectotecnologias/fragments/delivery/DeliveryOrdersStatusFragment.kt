package com.equipo1.proyectotecnologias.fragments.delivery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.equipo1.proyectotecnologias.R
import com.equipo1.proyectotecnologias.adapters.OrdenesRepartidorAdapter
import com.equipo1.proyectotecnologias.adapters.OrdenesTiendaAdapter
import com.equipo1.proyectotecnologias.adapters.OrdersClientAdapter
import com.equipo1.proyectotecnologias.adapters.ShopTabsPagerAdapter
import com.equipo1.proyectotecnologias.models.Order
import com.equipo1.proyectotecnologias.models.User
import com.equipo1.proyectotecnologias.providers.OrdersProvider
import com.equipo1.proyectotecnologias.utils.SharedPreg
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveryOrdersStatusFragment : Fragment() {

    var myView: View? = null
    var ordersProvider: OrdersProvider? = null
    var user: User? = null
    var sharedPref: SharedPreg? = null

    var recyclerViewOrders: RecyclerView? = null
    var adapter: OrdenesRepartidorAdapter? = null

    var status = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_delivery_orders_status, container, false)

        sharedPref = SharedPreg(requireActivity())

        status = arguments?.getString("status")!!

        getUserFromSession()
        ordersProvider = OrdersProvider()

        recyclerViewOrders = myView?.findViewById(R.id.recyclerview_orders)
        recyclerViewOrders?.layoutManager = LinearLayoutManager(requireContext())

        getOrders()

        return myView
    }


    private fun getOrders() {
        ordersProvider?.getOrdersByDeliveryAndStatus(user?.id!!,status)?.enqueue(object : Callback<ArrayList<Order>> {
            override fun onResponse(
                call: Call<ArrayList<Order>>,
                response: Response<ArrayList<Order>>
            ) {
                if (response.body() != null) {
                    val orders = response.body()
                    adapter = OrdenesRepartidorAdapter(requireActivity(), orders!!)
                    recyclerViewOrders?.adapter = adapter
                }

            }

            override fun onFailure(call: Call<ArrayList<Order>>, t: Throwable) {
                Toast.makeText(requireActivity(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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

}