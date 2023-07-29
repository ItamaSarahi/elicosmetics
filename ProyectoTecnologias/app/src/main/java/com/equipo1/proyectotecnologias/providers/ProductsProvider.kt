package com.equipo1.proyectotecnologias.providers

import android.icu.util.ULocale.Category
import com.equipo1.proyectotecnologias.api.ApiRoutes
import com.equipo1.proyectotecnologias.models.Product
import com.equipo1.proyectotecnologias.models.ResponseHttp
import com.equipo1.proyectotecnologias.models.User
import com.equipo1.proyectotecnologias.routes.CategoriesRoutes
import com.equipo1.proyectotecnologias.routes.ProductsRoutes
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class ProductsProvider() {
    private var productsRoutes: ProductsRoutes?=null

    init{
        var api=ApiRoutes()
        productsRoutes= api.getProductsRoutes()
    }


    fun findByCategory(idCategory: String): Call<ArrayList<Product>>? {
        return productsRoutes?.findByCategory(idCategory)
    }


    fun create(files: List<File>, product: Product): Call<ResponseHttp>? {

        val images = arrayOfNulls<MultipartBody.Part>(files.size)

        for (i in 0 until files.size) {
            val reqFile = RequestBody.create("image/*".toMediaTypeOrNull(), files[i])
            images[i] = MultipartBody.Part.createFormData("image", files[i].name, reqFile)
        }

        val requestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), product.toJson())
        return productsRoutes?.create(images, requestBody)
    }
}