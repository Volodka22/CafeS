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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity() {

    //TODO: delete static var

    companion object {
        val product = mutableListOf<Product>()
        val likeProducts = mutableListOf<Product>()
        val dislikeProducts = mutableListOf<Product>()
        var cafe = Cafe()
        val ordProd = mutableMapOf<Int, Int>()
        lateinit var badge: BadgeDrawable
    }

    private val LOGO_TIME:Long = 3000

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
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

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        try {
            transaction.commit()
        } catch (e: Exception) {
            Log.e("User", "Пользователь вышел до конца загрузки")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        ordProd.clear()
        product.clear()
        likeProducts.clear()
        dislikeProducts.clear()


        cafe = intent.getSerializableExtra("cafe") as Cafe


        Picasso.get().load(cafe.img).into(img)

        workWithDatabase()

        badge = bottomNavigation.getOrCreateBadge(R.id.navigation_basket)
        badge.isVisible = false

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        val timer = Timer()


        bottomNavigation.visibility = View.GONE

        timer.schedule(kotlin.concurrent.timerTask {
            runOnUiThread {
                img.visibility = View.GONE
                bottomNavigation.visibility = View.VISIBLE
            }
            openFragment(MenuFragment())
            timer.cancel()
        }, LOGO_TIME)

    }

    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.return_to_loginActivity))
            .setPositiveButton(
                R.string.yes
            ) { _, _ ->
                finish()
            }
            .setNegativeButton(
                R.string.no
            ) { _, _ ->
                // User cancelled the dialog
            }.show()
    }

    private fun workWithDatabase(){
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child(cafe.name).child("продукты")

        myRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                product.add(dataSnapshot.getValue<Product>(Product::class.java)!!)
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

}
