package com.equipo1.proyectotecnologias.fragments.client

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.equipo1.proyectotecnologias.R
import com.equipo1.proyectotecnologias.activities.MainActivity
import com.equipo1.proyectotecnologias.activities.client.update.ClientUpdateActivity
import com.equipo1.proyectotecnologias.activities.selectRolesActivity
import com.equipo1.proyectotecnologias.models.User
import com.equipo1.proyectotecnologias.utils.SharedPreg
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView

class ClientProfileFragment : Fragment() {
    var miView: View? = null
    var buttonSelectRol: Button? = null
    var buttonUpdateProfile: Button? = null
    var circleImageUser: CircleImageView? = null
    var textViewName: TextView? = null
    var textViewEmail: TextView? = null
    var textview_phone: TextView? = null
    var sharedPref: SharedPreg? = null
    var user: User? = null
    var imageViewSalir: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        miView = inflater.inflate(R.layout.fragment_client_profile, container, false)
        sharedPref = SharedPreg(requireActivity())

        buttonSelectRol = miView?.findViewById(R.id.btn_select_rol)

        buttonUpdateProfile = miView?.findViewById(R.id.btn_update_profile)
        textViewName = miView?.findViewById(R.id.textview_name)
        textViewEmail = miView?.findViewById(R.id.textview_email)
        textview_phone = miView?.findViewById(R.id.textview_phone)
        circleImageUser = miView?.findViewById(R.id.circleimage_user)
        imageViewSalir = miView?.findViewById(R.id.imageview_logout)


        buttonSelectRol?.setOnClickListener {
            goToSelectRol()
        }
        imageViewSalir?.setOnClickListener { logout() }

        buttonUpdateProfile?.setOnClickListener{
            goToUpdate()
        }


        getUserFromSession()

        textViewName?.text = "${user?.nombre} ${user?.apellido}"
        textViewEmail?.text = user?.email
        textview_phone?.text = user?.telefono

        if (!user?.image.isNullOrBlank()) {
            Glide.with(requireContext()).load(user?.image).into(circleImageUser!!)
        }



        return miView
    }

    private fun logout() {
        sharedPref?.remove("user")
        val i = Intent(requireContext(), MainActivity::class.java)
        startActivity(i)
    }

    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            //Si el usuario existe en sesion
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }

    }

    private fun goToUpdate() {
        val i = Intent(requireContext(), ClientUpdateActivity::class.java)
        startActivity(i)
    }


    private fun goToSelectRol() {
        val i = Intent(requireContext(), selectRolesActivity::class.java)
        //Eliminar el historial de pantallas
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }
}