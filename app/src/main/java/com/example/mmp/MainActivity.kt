package com.example.mmp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

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

        var ordProd = mutableMapOf<Product,Int>()
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

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        openFragment(MenuFragment())

    }

}
