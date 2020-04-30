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
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_basket.*
import java.util.*


class LoginActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var viewAdapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>
    private lateinit var viewManager: androidx.recyclerview.widget.RecyclerView.LayoutManager

    private var cafes = mutableListOf<Cafe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        searchInit()

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
        searchView.setQuery("", false)
    }

    private fun searchInit(){
        searchView.queryHint = "Поиск"

        searchView.setOnClickListener {
            mapCont.visibility = View.GONE
            searchView.setIconifiedByDefault(false)
        }

        searchView.setOnSearchClickListener {
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
    }

    private fun listInit(){

        viewManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        viewAdapter = SearchAdapter() {
            startActivity(
                Intent(
                    this@LoginActivity,
                    MainActivity::class.java
                ).putExtra("cafe", it)
            )
        }

        recyclerView.apply {

            setHasFixedSize(false)

            layoutManager = viewManager

            adapter = viewAdapter

        }

        FirebaseDatabase.getInstance().reference.child("Заведения")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val cafe = dataSnapshot.getValue<Cafe>(Cafe::class.java)!!
                    cafes.add(cafe)
                    mMap.addMarker(getLocationFromAddress(cafe.address)?.let {
                        MarkerOptions().position(
                            it
                        ).title(cafe.name)
                    })
                    (viewAdapter as SearchAdapter).add(cafe)
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {

                }

                override fun onCancelled(databaseError: DatabaseError) {

                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {

                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                }
            })

    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        listInit()


        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                getLocationFromAddress("Россия Калининград Кафедральный собор"),
                10F
            )
        )
        mMap.setOnMarkerClickListener {
            mapCont.visibility = View.GONE
            searchView.setIconifiedByDefault(false)
            searchView.setQuery(it.title, false)
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