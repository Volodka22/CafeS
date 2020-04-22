package com.example.mmp

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
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
        val product = mutableListOf<Product>()

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
            R.id.navigation_contacts -> {
                openFragment(ContactsFragment())
                return@OnNavigationItemSelectedListener true
            }
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


       /* AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                // ...
                if (FirebaseAuth.getInstance().currentUser != null) {
                    Log.e("Anime", FirebaseAuth.getInstance().currentUser?.displayName)
                } else {
                    Log.e("Anime", "Anime")
                }
            }
*/
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("products_nightclub")
        val myQuery = myRef.orderByChild("type")

        Log.d("AAAAAA", product.size.toString())

        myQuery.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                product.add(dataSnapshot.getValue<Product>(Product::class.java)!!)

                Log.d("AAAAAA", product.size.toString())

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
