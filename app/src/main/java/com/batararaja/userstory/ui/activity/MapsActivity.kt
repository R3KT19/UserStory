package com.batararaja.userstory.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.batararaja.userstory.*
import com.batararaja.userstory.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding : ActivityMapsBinding
    private lateinit var location: ArrayList<LatLng>
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        location = ArrayList()
        mainViewModel.getStoryMap()
        mainViewModel.listStory.observe(this, {data ->
            convert(data)
            Log.d(TAG, "onCreate: ${data.size}")
        })
        Thread.sleep(200)
        Log.d(TAG, "onCreateDiluar: ${location.size}")
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun convert(data: List<ListStoryItem>) {
        for (i in data.indices){
            val list = LatLng(
                data[i].lat,
                data[i].lon
            )
            Log.d(TAG, "convert: $i")
            location.add(list)
        }
        Log.d(TAG, "convert: ${location.size}")
    }

//    private fun change(data: List<ListStoryItem>?) {
//        if (data != null) {
//            for (i in 0..data.size){
//                location.add(location[i])
//            }
//        }
//    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        for (i in location.indices) {

            mMap.addMarker(MarkerOptions().position(location[i]).title("Marker"))

            // below lin is use to zoom our camera on map.
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location[i], 15f))

            // below line is use to move our camera to the specific location.
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location[i]))
        }
//        for (i in 0..location.size){
//            val marker = LatLng(location[i].lat, location[i].lon)
//            mMap.addMarker(
//                MarkerOptions()
//                    .position(marker)
//                    .title(location[i].name)
//                    .snippet(location[i].description)
//            )
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
//
//        }

//        val dicodingSpace = LatLng(-6.8957643, 107.6338462)
//        mMap.addMarker(
//            MarkerOptions()
//                .position(dicodingSpace)
//                .title("Dicoding Space")
//                .snippet("Batik Kumeli No.50")
//        )
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dicodingSpace, 15f))
//
//        val test = LatLng(-6.8957644, 107.6338462)
//        mMap.addMarker(
//            MarkerOptions()
//                .position(test)
//                .title("Test")
//                .snippet("Batik Kumeli No.50")
//        )
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(test))

        mMap.setOnMapLongClickListener { latLng ->
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("New Marker")
                    .snippet("Lat: ${latLng.latitude} Long: ${latLng.longitude}")
                    .icon(vectorToBitmap(R.drawable.ic_android, Color.parseColor("#3DDC84")))
            )
        }

        mMap.setOnPoiClickListener { pointOfInterest ->
            val poiMarker = mMap.addMarker(
                MarkerOptions()
                    .position(pointOfInterest.latLng)
                    .title(pointOfInterest.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            )
            poiMarker?.showInfoWindow()
        }

        getMyLocation()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
        if (vectorDrawable == null) {
            Log.e("BitmapHelper", "Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    companion object{
        private const val TAG = "Maps"
    }
}