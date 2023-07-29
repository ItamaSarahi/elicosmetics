package com.equipo1.proyectotecnologias.activities.client.update

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.equipo1.proyectotecnologias.R
import com.equipo1.proyectotecnologias.models.ResponseHttp
import com.equipo1.proyectotecnologias.models.User
import com.equipo1.proyectotecnologias.providers.UsersProvider
import com.equipo1.proyectotecnologias.utils.SharedPreg
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ClientUpdateActivity : AppCompatActivity() {

    val TAG = "ClientUpdateActivity"
    var circleImageUser: CircleImageView? = null
    var editTextName: EditText? = null
    var editTextLastname: EditText? = null
    var editTextPhone: EditText? = null
    var buttonUpdate: Button? = null

    var sharedPref: SharedPreg? = null
    var user: User? = null
    var usersProvider= UsersProvider()
    var toolbar:  androidx.appcompat.widget.Toolbar? =null

    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_update)

        toolbar=findViewById(R.id.toolbar)
        toolbar?.title="Editar perfil"
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPref = SharedPreg(this)
        circleImageUser = findViewById(R.id.circleimage_user_u)
        editTextName = findViewById(R.id.edittext_name)
        editTextLastname = findViewById(R.id.edittext_lastname)
        editTextPhone = findViewById(R.id.edittext_phone)
        buttonUpdate = findViewById(R.id.btn_confirmar)

        getUserFromSession()

        editTextName?.setText(user?.nombre)
        editTextLastname?.setText(user?.apellido)
        editTextPhone?.setText(user?.telefono)

        if (!user?.image.isNullOrBlank()) {
            Glide.with(this).load(user?.image).into(circleImageUser!!)
        }

        circleImageUser?.setOnClickListener { selectImage() }
        buttonUpdate?.setOnClickListener { updateData() }

    }

    private fun updateData() {

        val nombre = editTextName?.text.toString()
        val apellido = editTextLastname?.text.toString()
        val telefono = editTextPhone?.text.toString()

        user?.nombre = nombre
        user?.apellido = apellido
        user?.telefono = telefono

        if (imageFile != null) {
            usersProvider?.update(imageFile!!, user!!)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")

                    if(response.body()?.isSuccess==true) {
                        saveUserInSession(response.body()?.data.toString())
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(this@ClientUpdateActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }
        else {

            usersProvider?.updateWithoutImage(user!!)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")
                    if(response.body()?.isSuccess==true) {
                        saveUserInSession(response.body()?.data.toString())
                    }

                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(this@ClientUpdateActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }


    }

    private fun saveUserInSession(data: String) {
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref?.save("user", user)
    }

    private fun getUserFromSession() {

        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()) {
            // SI EL USARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }

    }

    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data
                imageFile = File(fileUri?.path) // EL ARCHIVO QUE VAMOS A GUARDAR COMO IMAGEN EN EL SERVIDOR
                circleImageUser?.setImageURI(fileUri)
            }
            else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(this, "Tarea se cancelo", Toast.LENGTH_LONG).show()
            }

        }

    private fun selectImage() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                startImageForResult.launch(intent)
            }
    }
}