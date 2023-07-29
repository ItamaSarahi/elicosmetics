package com.equipo1.proyectotecnologias.fragments.tienda

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.equipo1.proyectotecnologias.R
import com.equipo1.proyectotecnologias.models.Category
import com.equipo1.proyectotecnologias.models.ResponseHttp
import com.equipo1.proyectotecnologias.models.User
import com.equipo1.proyectotecnologias.providers.CategoriesProvider
import com.equipo1.proyectotecnologias.utils.SharedPreg
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.ImagePickerFileProvider
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class TiendaCategoryFragment : Fragment() {

    val TAG="TiendaCategoryFragment"
    var myView: View? = null
    var imageViewCategory: ImageView? = null
    var editTextCategory: EditText? = null
    var buttonCreate: Button? = null
    private var imageFile: File? = null
    var categoriesProvider: CategoriesProvider?=null
    var sharedPref: SharedPreg?=null
    var user: User?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_tienda_category, container, false)

        sharedPref= SharedPreg(requireActivity())
        //sharedPref = SharedPref(requireActivity())

        imageViewCategory = myView?.findViewById(R.id.imageview_category)
        editTextCategory = myView?.findViewById(R.id.edittext_category)
        buttonCreate = myView?.findViewById(R.id.btn_create)

        imageViewCategory?.setOnClickListener { selectImage() }
        buttonCreate?.setOnClickListener { createCategory() }

        getUserFromSession()
        categoriesProvider = CategoriesProvider()

        return myView
    }


    private fun getUserFromSession() {

        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()) {
            // SI EL USARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }

    }


    private fun createCategory() {
        val name = editTextCategory?.text.toString()
        if (imageFile != null) {
            val category = Category(name = name)


            categoriesProvider?.create(imageFile!!, category)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")

                    Toast.makeText(requireContext(),response.body()?.message,Toast.LENGTH_LONG).show()

                    if(response.body()?.isSuccess==true){
                        clearForm()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })

        }
        else {
            Toast.makeText(requireContext(), "Selecciona una imagen", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearForm(){
        editTextCategory?.setText("");
        imageFile = null
        imageViewCategory?.setImageResource(R.drawable.ic_image)

    }

    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data
                imageFile = File(fileUri?.path) // EL ARCHIVO QUE VAMOS A GUARDAR COMO IMAGEN EN EL SERVIDOR
                imageViewCategory?.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_LONG)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Tarea se cancelo", Toast.LENGTH_LONG).show()
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
    /*

    private fun getUserFromSession() {

        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()) {
            // SI EL USARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }

    }


    private fun clearForm() {
        editTextCategory?.setText("")
        imageFile = null
        imageViewCategory?.setImageResource(R.drawable.ic_image)
    }
*/



