package com.example.mmp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    //TODO: delete static var

    companion object {
        val product = arrayOf(Product("Супы","Борщ","Очень вкусно всем советую",100),
            Product("Супы","Борщ","Очень вкусно всем советую",100),
            Product("Супы","Борщ","Очень вкусно всем советую",100),
            Product("Супы","Борщ","Очень вкусно всем советую",100),
            Product("Супы","Борщ","Очень вкусно всем советую",100),
            Product("Супы","Борщ","Очень вкусно всем советую",100),
            Product("Супы","Борщ","Очень вкусно всем советую",100),
            Product("Супы","Борщ","Очень вкусно всем советую",100),
            Product("Супы","Борщ","Очень вкусно всем советую",100),
            Product("Супы","Борщ","Очень вкусно всем советую",100),
            Product("Супы","Борщ","Очень вкусно всем советую",100),
            Product("Супы","Борщ","Очень вкусно всем советую",100),
            Product("Супы","Борщ","Очень вкусно всем советую",100))

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

        badge = bottomNavigation.getOrCreateBadge(R.id.navigation_basket)
        badge.isVisible = false

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        openFragment(MenuFragment())

    }

}
