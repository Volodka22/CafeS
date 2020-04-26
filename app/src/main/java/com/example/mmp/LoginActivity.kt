package com.example.mmp


import android.content.Intent
import android.content.pm.ActivityInfo
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_basket.*
import java.util.*


class LoginActivity : FragmentActivity(), OnMapReadyCallback {

    lateinit var mMap: GoogleMap

    private lateinit var viewAdapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>
    private lateinit var viewManager: androidx.recyclerview.widget.RecyclerView.LayoutManager

    var cafes = mutableListOf<Cafe>()

    override fun onStart() {
        super.onStart()
        MainActivity.ordProd.clear()
        MainActivity.product.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val a = Cafe()
        a.address = "ул. Ялтинская, 81А, Калининград, Калининградская область"
        a.name = "Завод ночная смена"
        a.countTable = 6
        a.img = "http://zavod.sx/wp-content/uploads/2019/02/logo_cut.png"

        cafes.add(a)

        viewManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        viewAdapter = SearchAdapter(cafes.toTypedArray()) {
            startActivity(Intent(this@LoginActivity,
                MainActivity::class.java).putExtra("cafe",it))
        }

        recyclerView.apply {

            setHasFixedSize(false)

            layoutManager = viewManager

            adapter = viewAdapter

        }


        searchView.queryHint = "Поиск"

        searchView.setOnClickListener {
            mapCont.visibility = View.GONE
            searchView.setIconifiedByDefault(false)
        }

        searchView.setOnSearchClickListener{
            mapCont.visibility = View.GONE
            searchView.setIconifiedByDefault(false)
        }


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                (viewAdapter as SearchAdapter).filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean { //    adapter.getFilter().filter(newText);
                (viewAdapter as SearchAdapter).filter(newText)
                return false
            }
        })

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




    override fun onBackPressed() {
        mapCont.visibility = View.VISIBLE
        searchView.setIconifiedByDefault(true)
        searchView.setQuery("",false)
    }

    override fun onMapReady(googleMap: GoogleMap) {


        mMap = googleMap

        // Add a marker in Sydney and move the camera

        for(i in cafes){
            mMap.addMarker(getLocationFromAddress(i.address)?.let {
                MarkerOptions().position(
                    it
                ).title(i.name)
            })
        }


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getLocationFromAddress("Россия Калининград Кафедральный собор"),
            10F
        ))
        mMap.setOnMarkerClickListener {
            mapCont.visibility = View.GONE
            searchView.setIconifiedByDefault(false)
            searchView.setQuery(it.title,false)
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