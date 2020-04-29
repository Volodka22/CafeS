package com.example.mmp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_page.*


class PageFragment : Fragment() {

    private val ARG_OBJECT = "object"
    private var type:String = ""

    private lateinit var viewAdapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>
    private lateinit var viewManager: androidx.recyclerview.widget.RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_page, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {

            val a = mutableListOf<Product>()
            type = MenuFragment.tabTitles[getInt(ARG_OBJECT) - 1]

            MainActivity.product.forEach {
                if (it.type == MenuFragment.tabTitles[getInt(ARG_OBJECT) - 1])
                    a.add(it)
            }

            viewManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
            viewAdapter = PageRecyclerAdapter(pageRecyclerView.context) { product ->
                    val callAct = Intent(activity!!, ProductProfileActivity::class.java)
                    callAct.putExtra("cafe", MainActivity.cafe)
                    callAct.putExtra("product", product)

                    startActivityForResult(callAct, 1)
                }

            pageRecyclerView.apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = viewAdapter
            }

            workWithDatabase()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data!!.getIntExtra("cnt", 0) == 0)
            return

        MainActivity.badge.number += data.getIntExtra("cnt", 10)
        val product = MainActivity.product.indexOfFirst {
            it.name ==
                    (data.getSerializableExtra("product") as Product).name
        }
        if (MainActivity.ordProd[product] == null) {
            MainActivity.ordProd[product] =
                data.getIntExtra("cnt", 10)
        } else MainActivity.ordProd[product] = MainActivity.ordProd[product]!! +
                data.getIntExtra("cnt", 0)

        if (MainActivity.badge.number != 0)
            MainActivity.badge.isVisible = true
    }

    private fun workWithDatabase(){
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child(MainActivity.cafe.name).child("продукты")

        myRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val product = dataSnapshot.getValue<Product>(Product::class.java)!!
                if(product.type == type) (viewAdapter as PageRecyclerAdapter).add(product)
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