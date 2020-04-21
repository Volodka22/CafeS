package com.example.mmp

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity() {

    //TODO: delete static var

    companion object {
        val product = mutableListOf<Product>(/*Product("Супы","Борщ","Очень вкусно всем советую",100,"sdfs"),
            Product("Супы","Борщ","Очень вкусно всем советую",100,"sdfs"),
            Product("Супы","Борщ","Очень вкусно всем советую",100,"sdfs"),
            Product("Супы","Борщ","Очень вкусно всем советую",100,"sdfs"),
            Product("Супы","Борщ","Очень вкусно всем советую",100,"sdfs"),
            Product("Супы","Борщ","Очень вкусно всем советую",100,"sdfs"),
            Product("Супы","Борщ","Очень вкусно всем советую",100,"sdfs"),
            Product("Супы","Борщ","Очень вкусно всем советую",100,"sdfs"),
            Product("Супы","Борщ","Очень вкусно всем советую",100,"sdfs"),
            Product("Супы","Борщ","Очень вкусно всем советую",100,"sdfs"),
            Product("Супы","Борщ","Очень вкусно всем советую",100,"sdfs"),
            Product("Супы","Борщ","Очень вкусно всем советую",100,"sdfs"),
            Product("Супы","Борщ","Очень вкусно всем советую",100,"sdfs")*/)

        val ordProd = mutableMapOf<Product,Int>()
        lateinit var badge: BadgeDrawable
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_menu -> {
                openFragment(MenuFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_basket -> {
                openFragment(BasketFragment())
                return@OnNavigationItemSelectedListener true
            }
            /*R.id.navigation_contacts -> {
                openFragment(ContactsFragment())
                return@OnNavigationItemSelectedListener true
            }*/
        }
        false
    }

    private fun openFragment(fragment: Fragment){

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation =  (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("products_nightclub")
        val myQuery = myRef.orderByChild("type")

        Log.d("AAAAAA", product.size.toString())

        myQuery.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                product.add(dataSnapshot.getValue<Product>(Product::class.java)!!)

                Log.d("AAAAAA", product.size.toString())

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


        badge = bottomNavigation.getOrCreateBadge(R.id.navigation_basket)
        badge.isVisible = false

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        val timer = Timer()


        bottomNavigation.visibility = View.GONE

        timer.schedule(kotlin.concurrent.timerTask {
            runOnUiThread {
                bottomNavigation.visibility = View.VISIBLE
            }
            openFragment(MenuFragment())
            timer.cancel()
        },5000)



    }

}
