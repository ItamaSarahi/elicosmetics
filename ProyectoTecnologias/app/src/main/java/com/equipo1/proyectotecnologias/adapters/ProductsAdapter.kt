package com.equipo1.proyectotecnologias.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.equipo1.proyectotecnologias.R
import com.equipo1.proyectotecnologias.activities.client.home.ClientHomeActivity
import com.equipo1.proyectotecnologias.activities.client.products.detail.ClientProductsDetailActivity
import com.equipo1.proyectotecnologias.activities.delivery.home.DeliveryHomeActivity
import com.equipo1.proyectotecnologias.activities.makeup.home.MakeupHomeActivity
import com.equipo1.proyectotecnologias.models.Category
import com.equipo1.proyectotecnologias.models.Product
import com.equipo1.proyectotecnologias.models.Rol
import com.equipo1.proyectotecnologias.utils.SharedPreg
import com.google.gson.Gson

class ProductsAdapter(val context: Activity, val products: ArrayList<Product>): RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    val sharedPref = SharedPreg(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_product, parent, false)
        return ProductsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {

        val product = products[position] // CADA UNA DE LAS CATEGORIAS

        holder.textViewName.text = product.name
        holder.textViewPrice.text = "${product.price}$"
        Glide.with(context).load(product.image1).into(holder.imageViewProduct)


        holder.itemView.setOnClickListener { goToDetail(product) }
    }


    private fun goToDetail(product: Product) {
        val i = Intent(context, ClientProductsDetailActivity::class.java)
        i.putExtra("product", product.toJson())
        context.startActivity(i)
    }

    class ProductsViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewName: TextView
        val textViewPrice: TextView
        val imageViewProduct: ImageView

        init {
            textViewName = view.findViewById(R.id.textview_name)
            textViewPrice = view.findViewById(R.id.textview_price)
            imageViewProduct = view.findViewById(R.id.imageview_product)
        }

    }

}