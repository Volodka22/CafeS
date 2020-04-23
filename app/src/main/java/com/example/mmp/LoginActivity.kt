package com.example.mmp


import android.content.Intent
import android.content.pm.ActivityInfo
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class LoginActivity : FragmentActivity(), OnMapReadyCallback {

    // Request code for sign in
    private val RC_SIGN_IN = 123

    lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            // Already signed in
            startActivity(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(
                        listOf(
                            AuthUI.IdpConfig.GoogleBuilder().build()
                        )
                    )
                    .setTheme(R.style.AppTheme)
                    .setIsSmartLockEnabled(false)
                    .build()
            )
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        mMap.addMarker(getLocationFromAddress("ул. Ялтинская, 81А, Калининград, Калининградская область")?.let {
            MarkerOptions().position(
                it
            ).title("Ночной клуб завод")
        })
        

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getLocationFromAddress("Россия Калининград Кафедральный собор"),
            12F
        ))
        mMap.setOnMarkerClickListener {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            true
        }


    }


    private fun getLocationFromAddress(strAddress: String): LatLng? {

        val aLocale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Locale.Builder().setLanguage("RU").setScript("Latn").setRegion("RS").build()
        } else {
            Locale("RU")
        }

        val coder = Geocoder(this, aLocale)


        return try {
            val address = coder.getFromLocationName(strAddress, 1)
            val location = address[0];

            val p1 = LatLng(location.latitude, location.longitude);

            p1
        } catch (e: Exception) {

            null
        }
    }


}