package com.equipo1.proyectotecnologias.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.equipo1.proyectotecnologias.R
import com.equipo1.proyectotecnologias.activities.client.home.ClientHomeActivity
import com.equipo1.proyectotecnologias.models.ResponseHttp
import com.equipo1.proyectotecnologias.models.User
import com.equipo1.proyectotecnologias.providers.UsersProvider
import com.equipo1.proyectotecnologias.utils.SharedPreg
import com.github.dhaval2404.imagepicker.*
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SaveImageActivity : AppCompatActivity() {

     var circleImageUser : CircleImageView?=null
     var ButtonNext: Button?=null
     var ButtonConfirmar: Button?=null
     var usersProvider= UsersProvider()
     private var imageFile:File?=null
     var user:User?=null
     var sharedPreg : SharedPreg?=null
     val TAG="SaveImageActivity"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_image)
        sharedPreg= SharedPreg(this)
        getUserFromSession()
        circleImageUser = findViewById(R.id.circleImageUser)
        ButtonNext = findViewById(R.id.btn_next)
        ButtonConfirmar= findViewById(R.id.btn_confirm)


        circleImageUser?.setOnClickListener{selectImage()}
        ButtonNext?.setOnClickListener{goToClientHome()}
        ButtonConfirmar?.setOnClickListener{ saveImage() }

        }

    private fun saveImage() {

        if (imageFile != null && user != null) {
            usersProvider?.update(imageFile!!, user!!)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {

                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")
                    saveUserInSession(response.body()?.data.toString())
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(this@SaveImageActivity, "Error: ${t.message}", Toast.LENGTH_LONG)
                        .show()
                }

            })
    }else {
            Toast.makeText(
                this,
                "La imagen no puede ser nula ni tampoco los datos de sesion del usuario",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    private fun saveUserInSession(data:String){
        sharedPreg = SharedPreg(activity = this)
        val gson= Gson()
        val user = gson.fromJson(data,User::class.java)
        sharedPreg?.save("user",user)
        goToClientHome()
    }
    private  fun goToClientHome(){
        val i= Intent(this, ClientHomeActivity::class.java)
        //Eliminar el historial de pantallas
        i.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i);
    }

    private fun getUserFromSession(){
        val gson = Gson()
        if(!sharedPreg?.getData("user").isNullOrBlank()){
            //Si el usuario existe en sesion
           user = gson.fromJson(sharedPreg?.getData("user"), User::class.java)
        }

    }

        private val startImageForResult= registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode= result.resultCode
            val data = result.data

            if(resultCode== Activity.RESULT_OK){
                val fileUrl= data?.data
                imageFile = File(fileUrl?.path)//Archivo que se guardara como imagen en el servidor
                circleImageUser?.setImageURI(fileUrl)
            }else if(resultCode== ImagePicker.RESULT_ERROR){
                Toast.makeText(this,ImagePicker.getError(data),Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"Tarea se cancelo",Toast.LENGTH_LONG).show()
            }
        }

        private fun selectImage(){
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080,1080)
                .createIntent{intent->
                    startImageForResult.launch(intent)

                }

        }

}