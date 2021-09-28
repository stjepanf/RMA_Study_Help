package com.mindorks.framework.myapplication.activities

import android.content.Context
import android.location.Address
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mindorks.framework.myapplication.databinding.ActivityMapsBinding
import android.location.Geocoder
import com.google.android.gms.maps.model.MapStyleOptions
import com.mindorks.framework.myapplication.*
import kotlinx.android.synthetic.main.activity_maps.*
import java.lang.Exception
import android.app.Activity

import android.content.Intent
import android.net.Uri
import android.view.View
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var markerOptions: MarkerOptions? = null
    private lateinit var latLng: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        ivBack.setOnClickListener { finish() }
        tvSaveAddress.setOnClickListener {
            val addresses: List<Address>
            val geocoder: Geocoder = Geocoder(this, Locale.getDefault())

            addresses = geocoder.getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1
            )


            val address = addresses[0].getAddressLine(0)
            val returnIntent = Intent()
            returnIntent.putExtra(EXTRA_ADDRESS, address)
            setResult(RESULT_OK, returnIntent)
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (intent.getBooleanExtra(SHOW_MARKER, false)) {
            val location = getLocationFromAddress(this, intent.getStringExtra(EXTRA_ADDRESS))

            mMap.addMarker(
                MarkerOptions().position(location).title(intent.getStringExtra(EXTRA_TITLE))
            )
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17F))
            tvTitle.text = intent.getStringExtra(EXTRA_TITLE)
            tvDirections.visibility = View.VISIBLE
            tvDirections.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr="+intent.getStringExtra(EXTRA_ADDRESS))
                )
                startActivity(intent)
            }
        } else if (intent.getBooleanExtra(SELECT_ADDRESS, false)) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(45.554861, 18.695413), 15F))
            mMap.setOnMapClickListener {
                setMarkerOnMap(it)
            }

            tvTitle.text = getString(R.string.odaberi_lokaciju)
        }
    }

    private fun setMarkerOnMap(location: LatLng) {
        mMap.clear()
        markerOptions = MarkerOptions().position(location)
        mMap.addMarker(markerOptions)
        latLng = location
        tvSaveAddress.visibility = View.VISIBLE
    }

    private fun getLocationFromAddress(context: Context?, strAddress: String?): LatLng? {
        val coder = Geocoder(context)
        val address: List<Address>?
        var p1: LatLng? = null
        try {
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            val location: Address = address[0]
            location.latitude
            location.longitude
            p1 = LatLng(location.latitude, location.longitude)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return p1
    }
}