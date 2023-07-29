package com.equipo1.proyectotecnologias.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.equipo1.proyectotecnologias.R
import com.equipo1.proyectotecnologias.activities.client.home.ClientHomeActivity
import com.equipo1.proyectotecnologias.activities.delivery.home.DeliveryHomeActivity
import com.equipo1.proyectotecnologias.activities.makeup.home.MakeupHomeActivity
import com.equipo1.proyectotecnologias.models.ResponseHttp
import com.equipo1.proyectotecnologias.models.User
import com.equipo1.proyectotecnologias.providers.UsersProvider
import com.equipo1.proyectotecnologias.utils.SharedPreg
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class MainActivity : AppCompatActivity() {
    var TAG="Hola"
    var BotonRegistrar: Button? = null
    var  editTextEmail: EditText? =null
    var  editTextPassword: EditText? =null
    var ButtonLogin: Button?=null
    var ButtonGoogle: ImageView? =null
    var usersProvider = UsersProvider();

    private val RC_SIGN_IN = 123
    private lateinit var googleSignInClient: GoogleSignInClient




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "ELICOSMETICS"
        setContentView(R.layout.activity_main)
        BotonRegistrar = findViewById(R.id.btnRegistrar);
        editTextEmail = findViewById(R.id.edtEmail)
        editTextPassword= findViewById(R.id.edtPassword)
        ButtonLogin = findViewById(R.id.btnIniciarSesion)
        ButtonGoogle= findViewById(R.id.google)
        // Configuración de Google SignIn
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        BotonRegistrar?.setOnClickListener{//validar variable diferente de null
            goToRegister()
        }

        ButtonLogin?.setOnClickListener{login()}

        ButtonGoogle?.setOnClickListener {
            signInWithGoogle()
        }

        getUserFromSession()
    }

    // Función para manejar el inicio de sesión con Google
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                val email = account?.email.toString()
                val displayName = account?.displayName.toString()

                val user= User(
                    nombre=displayName,
                    apellido="",
                    email= email,
                    telefono="",
                    password= ""
                )

                usersProvider.login(email,"")?.enqueue(object: Callback<ResponseHttp>{
                    override fun onResponse(
                        call: Call<ResponseHttp>,
                        response: Response<ResponseHttp>
                    ) {
                        Log.d("MainActivity", "Response: ${response.body()}")

                        if(response.body()?.isSuccess==true){
                            saveUserInSession(response.body()?.data.toString())
                        }else{
                            usersProvider.register(user)?.enqueue(object: Callback<ResponseHttp>{
                                override fun onResponse(
                                    call: Call<ResponseHttp>,
                                    response: Response<ResponseHttp>
                                ) {
                                    if (response.body()?.isSuccess == true) {
                                        Log.d("Pruebas","si")
                                        saveUserInSession2(response.body()?.data.toString())
                                        goToClientHome()
                                    }
                                }
                                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                                    Toast.makeText(this@MainActivity,"Se produjo un error",Toast.LENGTH_LONG).show()
                                }
                            });
                        }
                    }
                    override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                        Toast.makeText(this@MainActivity,"Hubo un error ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })


            } catch (e: ApiException) {
                Toast.makeText(this, "Google SignIn failed", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun saveUserInSession2(data: String){
        var sharedPref = SharedPreg(this)
        val gson = Gson()
        val user = gson.fromJson(data,User::class.java)
        sharedPref.save("user",user)
    }

    private fun login(){
        val email = editTextEmail?.text.toString()
        val password = editTextPassword?.text.toString()

        if(isvalidateForm(email,password)) {
            usersProvider.login(email,password)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Log.d("MainActivity", "Response: ${response.body()}")

                     if(response.body()?.isSuccess==true){
                        Toast.makeText(this@MainActivity,response.body()?.message, Toast.LENGTH_LONG).show()
                         saveUserInSession(response.body()?.data.toString())
                    }else{
                         Toast.makeText(this@MainActivity,"Los datos no son correctos", Toast.LENGTH_LONG).show()
                     }
                }
                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@MainActivity,"Hubo un error ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }else{
            Toast.makeText(this,"El formulario no valido", Toast.LENGTH_LONG).show()
        }
    }

    private  fun goToClientHome(){
        val i= Intent(this,ClientHomeActivity::class.java)
        //Eliminar el historial de pantallas
        i.flags= FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i);
    }

    private  fun goToTiendaHome(){
        val i= Intent(this,MakeupHomeActivity::class.java)
        //Eliminar el historial de pantallas
        i.flags= FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i);
    }

    private  fun goToDeliveryHome(){
        val i= Intent(this,DeliveryHomeActivity::class.java)
        //Eliminar el historial de pantallas
        i.flags= FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i);
    }


    private fun goToSelectRol(){
        val i = Intent(this,selectRolesActivity::class.java)
        //Eliminar el historial de pantallas
        i.flags= FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun saveUserInSession(data:String){
        val sharedPrefe = SharedPreg(activity = this)
        val gson= Gson()
        val user = gson.fromJson(data,User::class.java)
        sharedPrefe.save("user",user)

        //El usuario tiene mas de un rol
        if(user.roles?.size!!>1){
            goToSelectRol()

        }else{ //El usuario solo tiene un rol
           goToClientHome()
        }

    }

    fun String.isEmailisValid():Boolean{
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun getUserFromSession(){

        val sharedPreg= SharedPreg(this)
        val gson = Gson()
        if(!sharedPreg.getData("user").isNullOrBlank()){
            //Si el usuario existe en sesion
            val user = gson.fromJson(sharedPreg.getData("user"), User::class.java)

            if(!sharedPreg.getData("rol").isNullOrBlank()){

                val rol = sharedPreg.getData("rol")?.replace("\"", "")
                if(rol=="TIENDA"){
                    goToTiendaHome()
                }
                if(rol=="CLIENTE"){
                    goToClientHome()
                }
                if(rol=="REPARTIDOR"){
                    goToDeliveryHome()
                }
            }else{
              goToClientHome()
            }
        }

    }


    private fun isvalidateForm(email:String, password:String): Boolean{
        if(email.isBlank()){
            return false
        }

        if(password.isBlank()){
            return false
        }

        if(!email.isEmailisValid()){
            return false
        }

        return true

    }
    private fun goToRegister(){
        val i= Intent(this,RegisterActivity::class.java)
        startActivity(i)
    }
}