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
import com.equipo1.proyectotecnologias.activities.delivery.home.DeliveryHomeActivity
import com.equipo1.proyectotecnologias.activities.makeup.home.MakeupHomeActivity
import com.equipo1.proyectotecnologias.models.Rol
import com.equipo1.proyectotecnologias.utils.SharedPreg

class RolesAdapter(val context: Activity, val roles: ArrayList<Rol>): RecyclerView.Adapter<RolesAdapter.RolesViewHolder>() {

    val sharedPref = SharedPreg(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RolesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_roles, parent, false)
        return RolesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return roles.size
    }

    override fun onBindViewHolder(holder: RolesViewHolder, position: Int) {

        val rol = roles[position] // CADA ROL

        holder.textViewRol.text = rol.name
        Glide.with(context).load(rol.image).into(holder.imageViewRol)

        holder.itemView.setOnClickListener { goToRol(rol) }
    }

    private fun goToRol(rol: Rol) {
        if (rol.name == "TIENDA") {
            sharedPref.save("rol","TIENDA")
            val i = Intent(context, MakeupHomeActivity::class.java)
            context.startActivity(i)
        }
        else if (rol.name == "CLIENTE") {
            sharedPref.save("rol","CLIENTE")
            val i = Intent(context, ClientHomeActivity::class.java)
            context.startActivity(i)
        }
        else if (rol.name == "REPARTIDOR") {
            sharedPref.save("rol","REPARTIDOR")
            val i = Intent(context, DeliveryHomeActivity::class.java)
            context.startActivity(i)
        }
    }

    class RolesViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewRol: TextView
        val imageViewRol: ImageView

        init {
            textViewRol = view.findViewById(R.id.text_rol)
            imageViewRol = view.findViewById(R.id.imageRol)
        }

    }

}