package com.equipo1.proyectotecnologias.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.equipo1.proyectotecnologias.R
import com.equipo1.proyectotecnologias.activities.client.ordenes.detalles.DetalleOrdenesCliente
import com.equipo1.proyectotecnologias.activities.delivery.ordenes.detalles.DetalleOrdenesRepartidorActivity
import com.equipo1.proyectotecnologias.activities.makeup.orders.detalles.DetalleOrdenesTiendaActivity
import com.equipo1.proyectotecnologias.models.Order

class OrdenesRepartidorAdapter (val context: Activity, val orders: ArrayList<Order>): RecyclerView.Adapter<OrdenesRepartidorAdapter.OrdersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_orders_tienda, parent, false)
        return OrdersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {

        val order = orders[position] // CADA UNA DE LAS ORDENES

        holder.textViewOrderId.text = "Orden #${order.id}"
        holder.textViewDate.text = "${order.timestamp}"
        holder.textViewAddress.text = "${order.address?.address}"
        holder.textViewClient.text = "${order.client?.nombre} ${order?.client?.apellido}"

        holder.itemView.setOnClickListener { goToOrderDetail(order) }
    }

    private fun goToOrderDetail(order: Order) {
        val i = Intent(context, DetalleOrdenesRepartidorActivity::class.java)
        i.putExtra("order", order.toJson())
        context.startActivity(i)
    }

    class OrdersViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewOrderId: TextView
        val textViewDate: TextView
        val textViewAddress: TextView
        val textViewClient: TextView

        init {
            textViewOrderId = view.findViewById(R.id.textview_order_id)
            textViewDate = view.findViewById(R.id.textview_date)
            textViewAddress = view.findViewById(R.id.textview_address)
            textViewClient = view.findViewById(R.id.textview_client)
        }

    }

}