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
import com.equipo1.proyectotecnologias.activities.client.products.list.ClientProductsListActivity
import com.equipo1.proyectotecnologias.activities.delivery.home.DeliveryHomeActivity
import com.equipo1.proyectotecnologias.activities.makeup.home.MakeupHomeActivity
import com.equipo1.proyectotecnologias.models.Category
import com.equipo1.proyectotecnologias.models.Rol
import com.equipo1.proyectotecnologias.utils.SharedPreg

class CategoriesAdapter(val context: Activity, val categories: ArrayList<Category>): RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    val sharedPref = SharedPreg(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_categories, parent, false)
        return CategoriesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {

        val category = categories[position] // CADA CATEGORIA

        holder.textViewCategory.text = category.name
        Glide.with(context).load(category.image).into(holder.imageViewCategory)

       holder.itemView.setOnClickListener { goToProducts(category) }
    }


    private fun goToProducts(category: Category) {
           val i = Intent(context, ClientProductsListActivity::class.java)
            i.putExtra("idCategory", category.id)
            context.startActivity(i)
    }

    class CategoriesViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewCategory: TextView
        val imageViewCategory: ImageView

        init {
            textViewCategory = view.findViewById(R.id.textview_category)
            imageViewCategory = view.findViewById(R.id.imageview_category)
        }

    }

}