package com.equipo1.proyectotecnologias.activities.delivery.ordenes.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.equipo1.proyectotecnologias.R
import com.equipo1.proyectotecnologias.activities.delivery.home.DeliveryHomeActivity
import com.equipo1.proyectotecnologias.models.Order
import com.equipo1.proyectotecnologias.models.ResponseHttp
import com.equipo1.proyectotecnologias.models.SocketEmit
import com.equipo1.proyectotecnologias.models.User
import com.equipo1.proyectotecnologias.providers.OrdersProvider
import com.equipo1.proyectotecnologias.utils.SharedPreg
import com.equipo1.proyectotecnologias.utils.SocketHandler
import com.equipo1.proyectotecnologias.utils.Whatssap
import com.github.nkzawa.socketio.client.Socket
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.maps.android.geometry.Point
import com.maps.route.extensions.drawRouteOnMap
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveryOrdersMapActivityActivity : AppCompatActivity(),  OnMapReadyCallback {

    val TAG = "DeliveryOrder"
    var googleMap: GoogleMap? = null

    val PERMISSION_ID = 42
    var fusedLocationClient: FusedLocationProviderClient? = null

    var address = ""
    var addressLatLng: LatLng?=null

    var markerDelivery: Marker?=null
    var markerAddress: Marker?=null

    var myLocationLatLng: LatLng?=null

    var order: Order?=null
    var gson = Gson()

    var textViewClient: TextView? = null
    var textViewAddress: TextView? = null
    var textViewNeighborhood: TextView? = null
    var buttonDelivered: Button? = null
    var circleImageUser: CircleImageView? = null
    var imageViewPhone: ImageView? = null
    val REQUEST_PHONE_CALL =40
    var sharedPref:SharedPreg?=null
    var user: User?=null
    var distanceBetween= 0.0f

    var ordersProvider: OrdersProvider?=null

    var socket: Socket?=null
    var contador=0;




    private val locationCallback = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult) {

            var lastLocation = locationResult.lastLocation
            myLocationLatLng= LatLng(lastLocation.latitude, lastLocation.longitude)
            emitPosition()

            distanceBetween= getDistanceBetween(myLocationLatLng!!,addressLatLng!!)

            if(distanceBetween<=100 && contador==0){
                var accessToken = "EAADdQ8nR92IBOZBZBNckmWzAZAIzWbZAZAfMHjbzoRam7WYrpBbySqZAiZChZBZBSDHydFGBVir9ZBKerdXdrj0vfjWg6tqnwNOG04UEltlOzbkL0zXfZCOFtSWjr00htDcGZAd85n9BgnMkZAHDtrkyewGZCQX7QSSVra6fFMI2fjvZAnrDACJ5OhwXAZAxvKiCqqmN6ytqqZAKasBPDyt8DEDteJ4YZD";
                var jsonBody = "{ \"messaging_product\": \"whatsapp\", \"to\": \"529515856562\", \"type\": \"template\", \"template\": { \"name\": \"hello_world\", \"language\": { \"code\": \"en_US\" } } }";
                val sendMessageTask = Whatssap(accessToken, jsonBody)
                sendMessageTask.execute()
                contador++;
            }

            Log.d(TAG,"Distancia: $distanceBetween")
            removeDeliveryMarker()
            addDeliveryMarker()
            Log.d("LOCALIZACION", "Callback: $lastLocation")

        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_orders_map_activity)

        sharedPref= SharedPreg(this)
        getUserFromSession()
        order= gson.fromJson(intent.getStringExtra("order"),Order::class.java)
        ordersProvider= OrdersProvider()
        addressLatLng =LatLng(order?.address?.lat!!, order?.address?.lng!!)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        textViewClient = findViewById(R.id.textview_client)
        textViewAddress = findViewById(R.id.textview_address)
        textViewNeighborhood = findViewById(R.id.textview_neighborhood)
        circleImageUser = findViewById(R.id.circleimage_user)
        imageViewPhone = findViewById(R.id.imageview_phone)
        buttonDelivered = findViewById(R.id.btn_delirevy)


        getLastLocation()

        textViewClient?.text = "${order?.client?.nombre} ${order?.client?.apellido}"
        textViewAddress?.text = order?.address?.address
        textViewNeighborhood?.text = order?.address?.neighborhood
        if (!order?.client?.image.isNullOrBlank()) {
            Glide.with(this).load(order?.client?.image).into(circleImageUser!!)
        }
        buttonDelivered?.setOnClickListener {

            if(distanceBetween<=350){
                updateOrder()
        }else{
                Toast.makeText(this,"Acercate mas al lugar de entrega",Toast.LENGTH_SHORT).show()
            }

        }
        imageViewPhone?.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
            }
            else {
                call()
            }

        }

        connectSocket()

    }

    private fun emitPosition() {
        val data = SocketEmit(
            id_order = order?.id!!,
            lat = myLocationLatLng?.latitude!!,
            lng = myLocationLatLng?.longitude!!
        )

        socket?.emit("position", data.toJson())
    }

    private fun connectSocket() {
        SocketHandler.setSocket()
        socket = SocketHandler.getSocket()
        socket?.connect()
    }


    override fun onDestroy(){
        super.onDestroy()

        if(locationCallback!=null && fusedLocationClient!=null){
            fusedLocationClient?.removeLocationUpdates(locationCallback)
        }

        socket?.disconnect()
    }

    private fun updateOrder() {
        ordersProvider?.updateToDelivered(order!!)?.enqueue(object: Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                if (response.body() != null) {

                    Toast.makeText(this@DeliveryOrdersMapActivityActivity, "${response.body()?.message}", Toast.LENGTH_LONG).show()

                    if (response.body()?.isSuccess == true) {
                        goToHome()
                    }

                }

            }
            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@DeliveryOrdersMapActivityActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun goToHome(){
        val i = Intent(this,DeliveryHomeActivity::class.java)
        i.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }

    private fun getDistanceBetween(fromLantLng:LatLng, toLatLng:LatLng):Float{
        var distance = 0.0f

        val from = Location("")
        val to = Location("")

        from.latitude = fromLantLng.latitude
        from.longitude = fromLantLng.longitude

        to.latitude = toLatLng.latitude
        to.longitude = toLatLng.longitude

        distance = from.distanceTo(to)


        return distance
    }




    private fun call() {
        val i = Intent(Intent.ACTION_CALL)
        i.data = Uri.parse("tel:${order?.client?.telefono}")

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permiso denegado para realizar la llamada", Toast.LENGTH_SHORT).show()
            return
        }

        startActivity(i)
    }


    private fun drawRoute() {
        val addressLocation = LatLng(order?.address?.lat!!, order?.address?.lng!!)
        googleMap?.drawRouteOnMap(
            getString(R.string.google_map_api_key),
            source = myLocationLatLng!!,
            destination = addressLocation,
            context = this,
            color = Color.BLACK,
            polygonWidth = 8,
            boundMarkers = false,
            markers = false

        )
    }


    private fun removeDeliveryMarker(){
        markerDelivery?.remove()
    }

    private fun addDeliveryMarker(){
        markerDelivery= googleMap?.addMarker(
            MarkerOptions()
                .position(myLocationLatLng)
                .title("mi posicion")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.delivery))
        )
    }


    private fun addAddressMarker(){
        var addresLocation=LatLng(order?.address?.lat!!,order?.address?.lng!!)
        markerAddress= googleMap?.addMarker(
            MarkerOptions()
                .position(addresLocation)
                .title("Lugar de entrega")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.entrega))
        )
    }


    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.uiSettings?.isZoomControlsEnabled=true
    }


    private fun updateLatLng(lat: Double, lng: Double) {
        order?.lat = lat
        order?.lng = lng

        ordersProvider?.updateLatLng(order!!)?.enqueue(object: Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                if (response.body() != null) {

                }

            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@DeliveryOrdersMapActivityActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getLastLocation() {
        if (checkPermission()) {

            if (isLocationEnabled()) {

                requestNewLocationData() //se inicia la posicion en tiemo real
                fusedLocationClient?.lastLocation?.addOnCompleteListener { task ->

                    var location = task.result

                    if(location!=null){
                        myLocationLatLng= LatLng(location.latitude, location.longitude)//aqui se guardan las posiciones del mapa
                        updateLatLng(location.latitude,location.longitude)
                        removeDeliveryMarker()
                        addDeliveryMarker()
                        addAddressMarker()
                        drawRoute()

                        googleMap?.moveCamera(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition.builder().target(
                                    LatLng(location.latitude, location.longitude)
                                ).zoom(15f).build()
                            ))
                    }
                }

            } else {
                Toast.makeText(this, "Habilita la localizacion", Toast.LENGTH_LONG).show()
                val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(i)
            }

        } else {
            requestPermissions()
        }
    }



    private fun requestNewLocationData() {
        val locationRequest = LocationRequest.create().apply {
            interval = 100
            fastestInterval = 50
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper() //inicializa la posicion en tiemo real
        )

    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        return false
    }


    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }


    private fun getUserFromSession() {

        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()) {
            // SI EL USARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
        if (requestCode == REQUEST_PHONE_CALL) {
            call()
        }
    }
}