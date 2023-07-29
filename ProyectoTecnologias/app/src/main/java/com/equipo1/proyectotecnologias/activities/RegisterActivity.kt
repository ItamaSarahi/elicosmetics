package com.equipo1.proyectotecnologias.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils

import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.equipo1.proyectotecnologias.R
import com.equipo1.proyectotecnologias.activities.client.home.ClientHomeActivity
import com.equipo1.proyectotecnologias.models.ResponseHttp
import com.equipo1.proyectotecnologias.models.User
import com.equipo1.proyectotecnologias.providers.UsersProvider
import com.equipo1.proyectotecnologias.utils.SharedPreg
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class RegisterActivity : AppCompatActivity() {

    val TAG="RegisterActivity"
    var ImageViewvolverInicio: ImageView? =null
    var editTextNombre: EditText?=null
    var editTextApellido: EditText?=null
    var editTextEmail: EditText?=null
    var editTextTelefono: EditText?=null
    var editTextContraseña: EditText?=null
    var editTextCContraseña: EditText?=null
    var buttonRegistrarse2: Button?=null
    var UsersProvider= UsersProvider()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        supportActionBar?.title = "ELICOSMETICS"
        ImageViewvolverInicio = findViewById(R.id.boton_regresar_login)
        editTextNombre = findViewById(R.id.nombre)
        editTextApellido = findViewById(R.id.apellido)
        editTextEmail = findViewById(R.id.email)
        editTextTelefono = findViewById(R.id.telefono)
        editTextContraseña = findViewById(R.id.contraseña)
        editTextCContraseña = findViewById(R.id.confirmarcontraseña)
        buttonRegistrarse2 = findViewById(R.id.btnregistrarse2)

        ImageViewvolverInicio?.setOnClickListener { goToLogin()}
        buttonRegistrarse2?.setOnClickListener{ register() }
    }

    private fun register(){
        val nombre = editTextNombre?.text.toString()
        val apellidos = editTextApellido?.text.toString()
        val correo = editTextEmail?.text.toString()
        val telefonos= editTextTelefono?.text.toString()
        val  contraseña= editTextContraseña?.text.toString()
        val confirmarContraseña = editTextCContraseña?.text.toString()


        if(isvalidateForm(nombre=nombre,telefonos=telefonos, apellidos = apellidos, correo=correo, contraseña=contraseña, confirmarContraseña=confirmarContraseña )) {
            val user= User(
                nombre=nombre,
                apellido=apellidos,
                email= correo,
                telefono=telefonos,
                password= contraseña
            )
            UsersProvider.register(user)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    if (response.body()?.isSuccess == true) {
                        saveUserInSession(response.body()?.data.toString())
                        goToClientHome()
                    }

                    Toast.makeText(this@RegisterActivity, response.body()?.message, Toast.LENGTH_LONG).show()

                    Log.d(TAG, "Response: ${response}" )
                    Log.d(TAG, "Body: ${response.body()}" )
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {

                    Log.d(TAG,"Se produjo un error ${t.message}")
                    Toast.makeText(this@RegisterActivity,"Se produjo un error",Toast.LENGTH_LONG).show()
                }

            });
        }

    }

    private fun goToClientHome(){
        val i = Intent(this,SaveImageActivity::class.java)
        i.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun saveUserInSession(data: String){
        var sharedPref = SharedPreg(this)
        val gson = Gson()
        val user = gson.fromJson(data,User::class.java)
        sharedPref.save("user",user)
    }
    fun String.isEmailisValid():Boolean{
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
    private fun isvalidateForm(nombre:String,
                               apellidos:String,
                               correo:String,
                               telefonos:String,
                               contraseña:String,
                               confirmarContraseña:String,
    ): Boolean{

        if(nombre.isBlank()){
            Toast.makeText(this,"Ingrese el nombre", Toast.LENGTH_LONG).show()
            return false
        }

        if(apellidos.isBlank()){
            Toast.makeText(this,"Ingrese el apellido", Toast.LENGTH_LONG).show()
            return false
        }

        if(correo.isBlank()){
            Toast.makeText(this,"Ingrese el email", Toast.LENGTH_LONG).show()
            return false
        }

        if(telefonos.isBlank()){
            Toast.makeText(this,"Ingrese el telefono", Toast.LENGTH_LONG).show()
            return false
        }
        if(contraseña.isBlank()){
            Toast.makeText(this,"Ingrese la contraseña", Toast.LENGTH_LONG).show()
            return false
        }

        if(confirmarContraseña.isBlank()){
            Toast.makeText(this,"Confirme la contraseña", Toast.LENGTH_LONG).show()
            return false
        }

        if(!correo.isEmailisValid()){
            return false
        }

        if(contraseña!=confirmarContraseña){
            Toast.makeText(this,"Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
            return false
        }

        return true

    }

    private fun goToLogin(){
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }


}
